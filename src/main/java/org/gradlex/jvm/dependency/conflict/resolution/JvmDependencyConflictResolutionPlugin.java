// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import static org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy.FIRST_MODULE;
import static org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy.HIGHEST_VERSION;

import java.util.Optional;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.plugins.JvmEcosystemPlugin;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class JvmDependencyConflictResolutionPlugin implements Plugin<Project> {
    public static final String MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME = "mainRuntimeClasspath";
    public static final String INTERNAL_CONFIGURATION_NAME = "internal";

    @Override
    public void apply(Project project) {
        JvmDependencyConflictDetectionPluginApplication.of(project).handleRulesMode();

        // Make sure 'jvm-ecosystem' is applied which adds the schemas for the attributes this plugin relies on
        project.getPlugins().apply(JvmEcosystemPlugin.class);

        JvmDependencyConflictsExtension jvmDependencyConflicts = project.getExtensions()
                .create(
                        "jvmDependencyConflicts",
                        JvmDependencyConflictsExtension.class,
                        project.getExtensions().getByType(SourceSetContainer.class));

        configureResolutionStrategies(project.getConfigurations(), jvmDependencyConflicts);
    }

    private void configureResolutionStrategies(
            ConfigurationContainer configurations, JvmDependencyConflictsExtension jvmDependencyConflicts) {
        configurations.configureEach(configuration -> {
            for (CapabilityDefinition definition : CapabilityDefinition.values()) {
                defineStrategy(definition, configuration, jvmDependencyConflicts);
            }
        });
    }

    private void defineStrategy(
            CapabilityDefinition definition,
            Configuration configuration,
            JvmDependencyConflictsExtension jvmDependencyConflicts) {
        CapabilitiesResolution resolution =
                configuration.getResolutionStrategy().getCapabilitiesResolution();
        resolution.withCapability(definition.getCapability(), details -> {
            if (!jvmDependencyConflicts
                    .getConflictResolution()
                    .getDeactivatedResolutionStrategies()
                    .get()
                    .contains(definition)) {
                if (definition.getDefaultStrategy() == HIGHEST_VERSION) {
                    details.selectHighestVersion();
                } else if (definition.getDefaultStrategy() == FIRST_MODULE) {
                    select(details, definition.getModules().get(0));
                }
            }
        });
    }

    private void select(CapabilityResolutionDetails details, String moduleGA) {
        Optional<ComponentVariantIdentifier> module = details.getCandidates().stream()
                .filter(c -> {
                    if (c.getId() instanceof ModuleComponentIdentifier) {
                        return ((ModuleComponentIdentifier) c.getId())
                                .getModuleIdentifier()
                                .toString()
                                .equals(moduleGA);
                    }
                    return false;
                })
                .findFirst();
        module.ifPresent(details::select);
    }
}
