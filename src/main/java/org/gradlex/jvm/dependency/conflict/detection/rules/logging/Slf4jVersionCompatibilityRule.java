/*
 * Copyright the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.jvm.dependency.conflict.detection.rules.logging;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.DirectDependencyMetadata;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;

import javax.inject.Inject;

/**
 * Slf4j 2 is a major breaking change from Slf4j 1.
 * This actually started in 1.8.0-alpha0 but 1.8 was renamed to 2 before GA.
 * <p>
 * Logback moved to Slf4j 2 in version 1.3, whereas Log4j 2 provides specific
 * artifacts for each Slf4j version.
 * <p>
 * Given the above:
 * * Slf4j implementations must use the same major version of `slf4j-api`
 * * `logback-classic` < 1.3 must use Slf4j 1
 * * `logback-classic` >= 1.3 must use Slf4j 2
 * * `log4j-slf4j-impl` must use Slf4j 1
 * * `log4j-slf4j2-impl` must use Slf4j 2
 */
@CacheableRule
public abstract class Slf4jVersionCompatibilityRule extends CapabilityDefinitionRule {

  @Inject
  public Slf4jVersionCompatibilityRule(CapabilityDefinition capabilityDefinition) {
    super(capabilityDefinition);
  }

  @Override
  protected String getVersion(ModuleVersionIdentifier id) {
    switch (id.getGroup()) {
      case "org.slf4j":
        return (id.getVersion().startsWith("1.")) ? "1.0" : "2.0";
      case "ch.qos.logback":
        return id.getVersion().matches("^(0\\.|1\\.[0-2]\\.)") ? "1.0" : "2.0";
      case "org.apache.logging.log4j":
        return id.getName().equals("log4-slf4j-impl") ? "1.0" : "2.0";
      default:
        throw new IllegalArgumentException("Unexpected component");
    }
  }

  @Override
  protected void additionalAdjustments(ModuleVersionIdentifier id, VariantMetadata variant) {
    String rejectedVersions;
    switch (getVersion(id)) {
      case"1.0":
        rejectedVersions = "[1.8.0-alpha0,)"; break;
      case "2.0":
        rejectedVersions = "[,1.8.0-alpha0)"; break;
      default:
        throw new UnsupportedOperationException();
    }
    variant.withDependencies(dependencies -> {
      for (DirectDependencyMetadata dependency : dependencies) {
        if (dependency.getGroup().equals("org.slf4j") && dependency.getName().equals("slf4j-api")) {
          dependency.version(version -> {
            version.reject(rejectedVersions);
          });
        }
      }
    });
  }
}
