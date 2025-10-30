// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.guava;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;

@CacheableRule
public abstract class GuavaListenableFutureRule extends CapabilityDefinitionRule {

    @Inject
    public GuavaListenableFutureRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        return "1.0";
    }

    @Override
    protected void additionalAdjustments(VariantMetadata variant) {
        // Despite publishing Gradle Metadata for Guava 32.1+, this part was not adopted eventually
        // See: https://github.com/google/guava/issues/6642#issuecomment-1656201382
        // Remove workaround dependency to '9999.0-empty-to-avoid-conflict-with-guava'
        variant.withDependencies(dependencies -> dependencies.removeIf(d -> "listenablefuture".equals(d.getName())));
    }
}
