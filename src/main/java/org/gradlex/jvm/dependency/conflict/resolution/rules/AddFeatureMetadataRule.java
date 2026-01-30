// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.jspecify.annotations.NullMarked;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities">
 *     component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities</a>,
 * <a href="https://blog.gradle.org/optional-dependencies">blog.gradle.org/optional-dependencies</a>
 */
@NullMarked
@CacheableRule
public abstract class AddFeatureMetadataRule implements ComponentMetadataRule {

    private final String classifier;

    @Inject
    public AddFeatureMetadataRule(String classifier) {
        this.classifier = classifier;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        addFeatureVariant(context, classifier, "Compile", "compile");
        addFeatureVariant(context, classifier, "Runtime", "runtime");
    }

    private static void addFeatureVariant(
            ComponentMetadataContext context, String classifier, String nameSuffix, String baseVariant) {
        ModuleVersionIdentifier id = context.getDetails().getId();
        String group = id.getGroup();
        String name = id.getName();
        String version = id.getVersion();

        context.getDetails().addVariant(classifier + nameSuffix, baseVariant, v -> {
            v.withCapabilities(c -> {
                c.removeCapability(group, name);
                c.addCapability(group, name + "-" + classifier, version);
            });
            v.withFiles(f -> {
                f.removeAllFiles();
                f.addFile(name + "-" + version + "-" + classifier + ".jar");
            });
        });
    }
}
