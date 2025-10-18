// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

/**
 * Rule adding a capability with a hard coded version.
 */
@CacheableRule
public abstract class FixedVersionCapabilityDefinitionRule extends CapabilityDefinitionRule {

    @Inject
    public FixedVersionCapabilityDefinitionRule(CapabilityDefinition capabilityDefinition) {
        super(capabilityDefinition);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        return "1.0";
    }
}
