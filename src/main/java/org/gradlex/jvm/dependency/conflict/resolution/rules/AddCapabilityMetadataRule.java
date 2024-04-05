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

package org.gradlex.jvm.dependency.conflict.resolution.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import javax.inject.Inject;
/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">
 *     component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>,
 * <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
 */
@CacheableRule
public abstract class AddCapabilityMetadataRule implements ComponentMetadataRule {

    private final String capability;

    @Inject
    public AddCapabilityMetadataRule(String capability) {
        this.capability = capability;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        ModuleVersionIdentifier id = context.getDetails().getId();
        String group = capability.split(":")[0];
        String name = capability.split(":")[1];
        String version = id.getVersion();

        context.getDetails().allVariants(v -> v.withCapabilities(c -> c.addCapability(group, name, version)));
    }
}
