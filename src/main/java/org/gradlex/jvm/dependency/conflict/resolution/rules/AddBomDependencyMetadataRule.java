// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.attributes.Category;
import org.gradle.api.model.ObjectFactory;
import org.jspecify.annotations.NullMarked;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">
 *     component_metadata_rules.html#fixing_wrong_dependency_details</a>
 */
@NullMarked
@CacheableRule
public abstract class AddBomDependencyMetadataRule implements ComponentMetadataRule {

    private final String bom;

    @Inject
    public AddBomDependencyMetadataRule(String bom) {
        this.bom = bom;
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails()
                .allVariants(v -> v.withDependencies(dependencies -> dependencies.add(
                        bom + ":" + version,
                        d -> d.attributes(a -> a.attribute(
                                Category.CATEGORY_ATTRIBUTE,
                                getObjects().named(Category.class, Category.REGULAR_PLATFORM))))));
    }
}
