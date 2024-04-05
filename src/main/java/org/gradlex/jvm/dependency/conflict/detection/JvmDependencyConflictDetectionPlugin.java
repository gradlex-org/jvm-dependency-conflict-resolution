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

package org.gradlex.jvm.dependency.conflict.detection;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.util.GradleVersion;
import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;

public class JvmDependencyConflictDetectionPlugin implements Plugin<ExtensionAware> {

    // Minimal version that works reliably with alignment, has the substitution rules `using` API
    // and has rulesMode setting in dependencyResolutionManagement
    private static final GradleVersion MINIMUM_SUPPORTED_VERSION = GradleVersion.version("6.8.3");

    @Override
    public void apply(ExtensionAware projectOrSettings) {
        if (GradleVersion.current().compareTo(MINIMUM_SUPPORTED_VERSION) < 0) {
            throw new IllegalStateException("Plugin requires at least Gradle " + MINIMUM_SUPPORTED_VERSION.getVersion());
        }

        ComponentMetadataHandler components;
        if (projectOrSettings instanceof Project) {
            // Make sure 'jvm-ecosystem' is applied which adds the schemas for the attributes this plugin relies on
            ((Project) projectOrSettings).getPlugins().apply("jvm-ecosystem");
            components = ((Project) projectOrSettings).getDependencies().getComponents();
        } else if (projectOrSettings instanceof Settings) {
            //noinspection UnstableApiUsage
            components = ((Settings) projectOrSettings).getDependencyResolutionManagement().getComponents();
        } else {
            throw new IllegalStateException("Cannot apply plugin to: " + projectOrSettings.getClass().getName());
        }
        registerRules(components);
    }

    private void registerRules(ComponentMetadataHandler components) {
        for (CapabilityDefinition definition : CapabilityDefinition.values()) {
            registerCapabilityRule(definition, components);
        }
        for (AlignmentDefinition definition : AlignmentDefinition.values()) {
            registerAlignmentRule(definition, components);
        }
    }

    private void registerCapabilityRule(CapabilityDefinition definition, ComponentMetadataHandler components) {
        for (String module : definition.getModules()) {
            components.withModule(module, definition.getRuleClass(), ac -> ac.params(definition));
        }
    }

    private void registerAlignmentRule(AlignmentDefinition definition, ComponentMetadataHandler components) {
        for (String module : definition.getModules()) {
            components.withModule(module, definition.getRuleClass(), ac -> ac.params(definition));
        }
    }
}
