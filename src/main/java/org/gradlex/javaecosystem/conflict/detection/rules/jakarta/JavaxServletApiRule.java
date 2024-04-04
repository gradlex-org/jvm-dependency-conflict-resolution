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

package org.gradlex.javaecosystem.conflict.detection.rules.jakarta;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.javaecosystem.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.javaecosystem.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.javaecosystem.conflict.detection.util.VersionNumber;

import javax.inject.Inject;

@CacheableRule
public abstract class JavaxServletApiRule extends CapabilityDefinitionRule {

    static final String FIRST_JAKARTA_VERSION = "5.0.0";

    @Inject
    public JavaxServletApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(getVersion(id)).compareTo(VersionNumber.parse(JavaxServletApiRule.FIRST_JAKARTA_VERSION)) < 0;
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        if (id.getGroup().startsWith("org.apache.tomcat")) {
            return servletApiVersionForTomcatVersion(VersionNumber.parse(id.getVersion()));
        }
        return id.getVersion();
    }

    // https://tomcat.apache.org/whichversion.html
    static String servletApiVersionForTomcatVersion(VersionNumber tomcatVersion) {
        if (tomcatVersion.compareTo(VersionNumber.version(10, 1)) >= 0) {
            return "6.0.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(10, 0)) >= 0) {
            return "5.0.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(9, 0)) >= 0) {
            return "4.0.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(8, 0)) >= 0) {
            return "3.1.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(7, 0)) >= 0) {
            return "3.0.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(6, 0)) >= 0) {
            return "2.5.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(5, 5)) >= 0) {
            return "2.4.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(4, 1)) >= 0) {
            return "2.3";
        }
        return "2.2";
    }
}
