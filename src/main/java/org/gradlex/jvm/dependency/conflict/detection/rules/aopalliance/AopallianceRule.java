// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.aopalliance;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;
import org.jspecify.annotations.NullMarked;

@NullMarked
@CacheableRule
public abstract class AopallianceRule extends CapabilityDefinitionRule {

    // the conflict starts from Spring 4.3.0, before that it is effectively a correct dependency
    private static final String FIRST_AOP_EMBEDDED_VERSION = "4.3.0";

    @Inject
    public AopallianceRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return "aopalliance".equals(id.getGroup())
                || VersionNumber.parse(id.getVersion()).compareTo(VersionNumber.parse(FIRST_AOP_EMBEDDED_VERSION)) >= 0;
    }
}
