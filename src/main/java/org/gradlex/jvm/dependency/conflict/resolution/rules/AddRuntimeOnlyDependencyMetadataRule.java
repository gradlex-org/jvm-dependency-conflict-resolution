// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import static org.gradlex.jvm.dependency.conflict.resolution.rules.DependencyModification.addCapability;
import static org.gradlex.jvm.dependency.conflict.resolution.rules.VariantSelection.allVariantsMatching;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.Usage;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">
 * component_metadata_rules.html#fixing_wrong_dependency_details</a>
 */
@CacheableRule
public abstract class AddRuntimeOnlyDependencyMetadataRule implements ComponentMetadataRule {

    private final String dependency;
    private final String capability;

    @Inject
    public AddRuntimeOnlyDependencyMetadataRule(String dependency, String capability) {
        this.dependency = dependency;
        this.capability = capability;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        allVariantsMatching(
                context,
                id -> id.matches(Usage.USAGE_ATTRIBUTE, Usage.JAVA_RUNTIME)
                        && id.matches(Category.CATEGORY_ATTRIBUTE, Category.LIBRARY),
                v -> v.withDependencies(dependencies -> dependencies.add(dependency, d -> {
                    if (!capability.isEmpty()) addCapability(d, capability);
                })));
    }
}
