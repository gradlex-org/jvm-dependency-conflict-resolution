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
public abstract class JakartaActivationImplementationRule extends CapabilityDefinitionRule {

    // Starting with this version the implementation moved to the 'org.eclipse' package and is no longer a conflict
    public static final String FIRST_ECLIPSE_VERSION = "2.0.0";

    @Inject
    public JakartaActivationImplementationRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return isSunJakartaActivationImpl(id) || isAngusJakartaActivationImpl(id);
    }

    private boolean isSunJakartaActivationImpl(ModuleVersionIdentifier id) {
        return "com.sun.activation".equals(id.getGroup())
                && VersionNumber.parse(id.getVersion())
                                .compareTo(VersionNumber.parse(JavaxActivationApiRule.FIRST_JAKARTA_VERSION))
                        >= 0;
    }

    private boolean isAngusJakartaActivationImpl(ModuleVersionIdentifier id) {
        return "org.eclipse.angus".equals(id.getGroup())
                && VersionNumber.parse(id.getVersion()).compareTo(VersionNumber.parse(FIRST_ECLIPSE_VERSION)) < 0;
    }
}
