// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import static org.gradlex.jvm.dependency.conflict.resolution.rules.VariantSelection.allVariantsMatching;

import java.util.stream.Collectors;
import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.Usage;
import org.jspecify.annotations.NullMarked;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">
 * component_metadata_rules.html#fixing_wrong_dependency_details</a>
 */
@NullMarked
@CacheableRule
public abstract class ReduceToCompileOnlyApiDependencyMetadataRule implements ComponentMetadataRule {

    private final String dependency;

    @Inject
    public ReduceToCompileOnlyApiDependencyMetadataRule(String dependency) {
        this.dependency = dependency;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        allVariantsMatching(
                context,
                id -> id.matches(Usage.USAGE_ATTRIBUTE, Usage.JAVA_RUNTIME)
                        && id.matches(Category.CATEGORY_ATTRIBUTE, Category.LIBRARY),
                v -> v.withDependencies(d -> d.removeAll(d.stream()
                        .filter(it -> dependency.equals(it.getModule().toString()))
                        .collect(Collectors.toList()))));
    }
}
