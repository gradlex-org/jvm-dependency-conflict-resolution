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

package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.VariantMetadata;

import javax.inject.Inject;

@CacheableRule
public abstract class EnumBasedRule implements ComponentMetadataRule {

    private final CapabilityDefinitions rule;

    @Inject
    public EnumBasedRule(CapabilityDefinitions rule) {
        this.rule = rule;
    }

    @Override
    public final void execute(ComponentMetadataContext context) {
        if (shouldApply(context.getDetails().getId())) {
            context.getDetails().allVariants(variant -> {
                variant.withCapabilities(capabilities -> capabilities.addCapability(
                        rule.getGroup(), rule.getCapabilityName(), getVersion(context.getDetails().getId())
                ));
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

    protected void additionalAdjustments(VariantMetadata variant) { }
}
