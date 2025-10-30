// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">
 *     component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>,
 * <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
 */
@CacheableRule
public abstract class RemoveCapabilityMetadataRule implements ComponentMetadataRule {

    private final String capability;

    @Inject
    public RemoveCapabilityMetadataRule(String capability) {
        this.capability = capability;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        ModuleVersionIdentifier id = context.getDetails().getId();
        String group = capability.split(":")[0];
        String name = capability.split(":")[1];

        context.getDetails().allVariants(v -> v.withCapabilities(c -> c.removeCapability(group, name)));
    }
}
