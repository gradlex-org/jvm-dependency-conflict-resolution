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
public abstract class JakartaAnnotationApiRule extends CapabilityDefinitionRule {

    @Inject
    public JakartaAnnotationApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        if ("org.apache.tomcat".equals(id.getGroup())) {
            return annotationApiVersionForTomcatVersion(VersionNumber.parse(id.getVersion()));
        }
        return id.getVersion();
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(getVersion(id))
                        .compareTo(VersionNumber.parse(JavaxAnnotationApiRule.FIRST_JAKARTA_VERSION))
                >= 0;
    }

    // This is probably 100% accurate - older Tomcat versions might ship older 1.x specs
    private static String annotationApiVersionForTomcatVersion(VersionNumber tomcatVersion) {
        if (tomcatVersion.compareTo(VersionNumber.version(10, 0)) >= 0) {
            return "2.1.0";
        }
        return "1.3.0";
    }
}
