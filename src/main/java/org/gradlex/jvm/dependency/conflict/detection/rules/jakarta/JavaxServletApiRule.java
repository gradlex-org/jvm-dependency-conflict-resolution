// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;
import org.jspecify.annotations.NullMarked;

@NullMarked
@CacheableRule
public abstract class JavaxServletApiRule extends CapabilityDefinitionRule {

    static final String FIRST_JAKARTA_VERSION = "5.0.0";

    @Inject
    public JavaxServletApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(getVersion(id))
                        .compareTo(VersionNumber.parse(JavaxServletApiRule.FIRST_JAKARTA_VERSION))
                < 0;
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
