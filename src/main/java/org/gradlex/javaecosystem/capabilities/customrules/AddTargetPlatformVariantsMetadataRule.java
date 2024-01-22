/*
 * Copyright 2022 the GradleX team.
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

package org.gradlex.javaecosystem.capabilities.customrules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.MachineArchitecture;
import org.gradle.nativeplatform.OperatingSystemFamily;

import javax.inject.Inject;
import java.util.List;

import static org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE;
import static org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_variants_for_native_jars">
 *     component_metadata_rules.html#adding_variants_for_native_jars</a>
 */
@CacheableRule
@NonNullApi
public abstract class AddTargetPlatformVariantsMetadataRule implements ComponentMetadataRule {

    private final List<Target> targets;

    @Inject
    abstract protected ObjectFactory getObjects();

    @Inject
    public AddTargetPlatformVariantsMetadataRule(List<Target> targets) {
        this.targets = targets;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();

        for (Target target : targets) {
            if (target.getLabel().isEmpty()) {
                details.withVariant("compile", variant -> {
                    configureAttributes(target, variant);
                });
                details.withVariant("runtime", variant -> {
                    configureAttributes(target, variant);
                });
            } else {
                addTargetPlatformVariant(target, details, "Compile", "compile");
                addTargetPlatformVariant(target, details, "Runtime", "runtime");
            }
        }
    }

    private void addTargetPlatformVariant(Target target, ComponentMetadataDetails details, String nameSuffix, String baseVariant) {
        String name = details.getId().getName();
        String version = details.getId().getVersion();

        details.addVariant(target.getLabel() + nameSuffix, baseVariant, variant -> {
            configureAttributes(target, variant);
            variant.withFiles(files -> {
                files.removeAllFiles();
                files.addFile(name + "-" + version + "-" + target.getLabel() + ".jar");
            });
        });
    }

    private void configureAttributes(Target target, VariantMetadata variant) {
        variant.attributes(attributes -> {
            attributes.attribute(OPERATING_SYSTEM_ATTRIBUTE, getObjects().named(OperatingSystemFamily.class, target.getOperatingSystem()));
            attributes.attribute(ARCHITECTURE_ATTRIBUTE, getObjects().named(MachineArchitecture.class, target.getArchitecture()));
        });
    }
}
