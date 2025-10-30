// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.VariantMetadata;

@CacheableRule
public abstract class CapabilityDefinitionRule implements ComponentMetadataRule {

    private final CapabilityDefinition definition;

    @Inject
    public CapabilityDefinitionRule(CapabilityDefinition definition) {
        this.definition = definition;
    }

    @Override
    public final void execute(ComponentMetadataContext context) {
        if (shouldApply(context.getDetails().getId())) {
            context.getDetails().allVariants(variant -> {
                variant.withCapabilities(capabilities -> {
                    // remove capability if it already exists so that it can be added back
                    // with a potentially different version
                    capabilities.removeCapability(definition.getGroup(), definition.getCapabilityName());
                    capabilities.addCapability(
                            definition.getGroup(),
                            definition.getCapabilityName(),
                            getVersion(context.getDetails().getId()));
                });
                additionalAdjustments(variant);
            });
        }
    }

    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return true;
    }

    protected String getVersion(ModuleVersionIdentifier id) {
        return id.getVersion();
    }

    protected void additionalAdjustments(VariantMetadata variant) {}
}
