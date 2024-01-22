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
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import javax.inject.Inject;
import java.util.List;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities">
 *     component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities</a>,
 * <a href="https://blog.gradle.org/optional-dependencies">blog.gradle.org/optional-dependencies</a>
 */
@CacheableRule
@NonNullApi
public abstract class AddFeaturesMetadataRule implements ComponentMetadataRule {

    private final List<String> classifiers;

    @Inject
    public AddFeaturesMetadataRule(List<String> classifiers) {
        this.classifiers = classifiers;
    }

    @Override
    public void execute(ComponentMetadataContext context) {

        for (String classifier : classifiers) {
            addFeatureVariant(context, classifier, "Compile", "compile");
            addFeatureVariant(context, classifier, "Runtime", "runtime");
        }
    }

    private static void addFeatureVariant(ComponentMetadataContext context, String classifier, String nameSuffix, String baseVariant) {
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
