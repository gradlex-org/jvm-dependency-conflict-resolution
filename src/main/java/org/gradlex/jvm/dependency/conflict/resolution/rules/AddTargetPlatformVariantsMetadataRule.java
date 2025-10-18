// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import static org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE;
import static org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.MachineArchitecture;
import org.gradle.nativeplatform.OperatingSystemFamily;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_variants_for_native_jars">
 *     component_metadata_rules.html#adding_variants_for_native_jars</a>
 */
@CacheableRule
public abstract class AddTargetPlatformVariantsMetadataRule implements ComponentMetadataRule {

    private final String feature;
    private final String classifier;
    private final String operatingSystem;
    private final String architecture;

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    public AddTargetPlatformVariantsMetadataRule(
            String feature, String classifier, String operatingSystem, String architecture) {
        this.feature = feature;
        this.classifier = classifier;
        this.operatingSystem = operatingSystem;
        this.architecture = architecture;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        if (classifier.isEmpty()) {
            if (!feature.isEmpty()) {
                throw new IllegalStateException("if classifier is empty, feature must be empty too.");
            }
            details.withVariant("compile", this::configureAttributes);
            details.withVariant("runtime", this::configureAttributes);
        } else {
            addTargetPlatformVariant(details, "Compile", "compile");
            addTargetPlatformVariant(details, "Runtime", "runtime");
        }
    }

    private void addTargetPlatformVariant(ComponentMetadataDetails details, String nameSuffix, String baseVariant) {
        String group = details.getId().getGroup();
        String name = details.getId().getName();
        String version = details.getId().getVersion();

        details.addVariant(classifier + nameSuffix, baseVariant, variant -> {
            if (!feature.isEmpty()) {
                variant.withCapabilities(c -> {
                    c.removeCapability(group, name);
                    c.addCapability(group, name + "-" + feature, version);
                });
            }
            configureAttributes(variant);
            variant.withFiles(files -> {
                files.removeAllFiles();
                files.addFile(name + "-" + version + "-" + classifier + ".jar");
            });
        });
    }

    private void configureAttributes(VariantMetadata variant) {
        variant.attributes(attributes -> {
            attributes.attribute(
                    OPERATING_SYSTEM_ATTRIBUTE, getObjects().named(OperatingSystemFamily.class, operatingSystem));
            attributes.attribute(ARCHITECTURE_ATTRIBUTE, getObjects().named(MachineArchitecture.class, architecture));
        });
    }
}
