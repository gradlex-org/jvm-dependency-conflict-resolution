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

package org.gradlex.javaecosystem.capabilities;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions;
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static org.gradlex.javaecosystem.capabilities.JavaEcosystemCapabilitiesBasePlugin.basePluginNotYetRegisteredInSettings;
import static org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy.FIRST_MODULE;
import static org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy.HIGHEST_VERSION;

public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        BasePluginApplication.of(project).handleRulesMode();

        JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities = project.getExtensions().create("javaEcosystemCapabilities", JavaEcosystemCapabilitiesExtension.class);

        configureResolutionStrategies(project.getConfigurations(), javaEcosystemCapabilities);
    }

    private void configureResolutionStrategies(ConfigurationContainer configurations, JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities) {
        configurations.all(configuration -> {
            for (CapabilityDefinitions definition : CapabilityDefinitions.values()) {
                defineStrategy(definition, configuration, javaEcosystemCapabilities);
            }
        });
    }

    private void defineStrategy(CapabilityDefinitions definition, Configuration configuration, JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities) {
        CapabilitiesResolution resolution = configuration.getResolutionStrategy().getCapabilitiesResolution();
        resolution.withCapability(definition.getCapability(), details -> {
            if (!javaEcosystemCapabilities.getDeactivatedResolutionStrategies().get().contains(definition)) {
                if (definition.defaultStrategy == HIGHEST_VERSION) {
                    details.selectHighestVersion();
                } else if (definition.defaultStrategy == FIRST_MODULE) {
                    select(details, definition.modules.get(0));
                }
            }
        });
    }

    private void select(CapabilityResolutionDetails details, String moduleGA) {
        Optional<ComponentVariantIdentifier> module = details.getCandidates().stream().filter(c -> {
            if (c.getId() instanceof ModuleComponentIdentifier) {
                return ((ModuleComponentIdentifier) c.getId()).getModuleIdentifier().toString().equals(moduleGA);
            }
            return false;
        }).findFirst();
        module.ifPresent(details::select);
    }
}
