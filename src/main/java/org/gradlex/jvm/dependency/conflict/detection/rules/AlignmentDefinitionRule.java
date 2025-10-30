// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.attributes.Category;
import org.gradle.api.model.ObjectFactory;

@CacheableRule
public abstract class AlignmentDefinitionRule implements ComponentMetadataRule {

    private final AlignmentDefinition definition;

    @Inject
    public AlignmentDefinitionRule(AlignmentDefinition definition) {
        this.definition = definition;
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public final void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        if (shouldApply(details.getId())) {
            if (definition.hasBom()) {
                applyWithBom(details);
            } else {
                applyWithoutBom(details);
            }
        }
    }

    void applyWithBom(ComponentMetadataDetails details) {
        String version = details.getId().getVersion();
        details.allVariants(v -> v.withDependencies(dependencies -> dependencies.add(
                definition.getBom() + ":" + version,
                d -> d.attributes(a -> a.attribute(
                        Category.CATEGORY_ATTRIBUTE, getObjects().named(Category.class, Category.REGULAR_PLATFORM))))));
    }

    void applyWithoutBom(ComponentMetadataDetails details) {
        String version = details.getId().getVersion();
        String group = details.getId().getGroup();
        details.allVariants(v -> {
            v.withDependencyConstraints(c -> {
                for (String member : definition.getModules()) {
                    if (member.contains(":")) {
                        c.add(member + ":" + version);
                    } else {
                        c.add(group + ":" + member + ":" + version);
                    }
                }
            });
        });
    }

    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return true;
    }
}
