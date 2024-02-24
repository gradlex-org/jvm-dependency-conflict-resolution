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

import org.gradle.api.NonNullApi;
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

@NonNullApi
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

        /*
        standardResolutionStrategy.put(AopallianceRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBcmailRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBcpgRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBcpkixRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBcprovRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBctlsRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBctspRule.CAPABILITY, null);
        standardResolutionStrategy.put(BouncycastleBcutilRule.CAPABILITY, null);
        standardResolutionStrategy.put(C3p0Rule.CAPABILITY, null);
        standardResolutionStrategy.put(CGlibRule.CAPABILITY, null);
        standardResolutionStrategy.put(CommonsIoRule.CAPABILITY, null);
        standardResolutionStrategy.put(Dom4jRule.CAPABILITY, null);
        standardResolutionStrategy.put(FindbugsAnnotationsRule.CAPABILITY, null);
        standardResolutionStrategy.put(GoogleCollectionsRule.CAPABILITY, null);
        standardResolutionStrategy.put(GuavaListenableFutureRule.CAPABILITY, null);
        standardResolutionStrategy.put(GuavaRule.CAPABILITY, null);
        standardResolutionStrategy.put(HikariCPRule.CAPABILITY, null);
        standardResolutionStrategy.put(IntellijAnnotationsRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaActivationApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaActivationImplementationRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaAnnotationApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaJsonApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaServletApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaWebsocketApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaWebsocketClientApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JakartaWsRsApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaAssistRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxActivationApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxAnnotationApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxEjbApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxElApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxInjectApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxJsonApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxJwsApisRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxMailApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxPersistenceApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxServletJspRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxServletJstlRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxSoapApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxValidationApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxWebsocketApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxWsRsApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxXmlBindApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JavaxXmlWsApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(JcipAnnotationsRule.CAPABILITY, null);
        standardResolutionStrategy.put(JnaPlatformRule.CAPABILITY, null);
        standardResolutionStrategy.put(JtsCoreRule.CAPABILITY, null);
        standardResolutionStrategy.put(JtsRule.CAPABILITY, null);
        standardResolutionStrategy.put(JunitRule.CAPABILITY, null);
        standardResolutionStrategy.put(PostgresqlRule.CAPABILITY, null);
        standardResolutionStrategy.put(StaxApiRule.CAPABILITY, null);
        standardResolutionStrategy.put(VelocityRule.CAPABILITY, null);
        standardResolutionStrategy.put(WoodstoxAslRule.CAPABILITY, null);
        */
    }

    private void defineStrategy(CapabilityDefinitions definition, Configuration configuration, JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities) {
        CapabilitiesResolution resolution = configuration.getResolutionStrategy().getCapabilitiesResolution();
        resolution.withCapability(definition.getCapability(), details -> {
            if (!javaEcosystemCapabilities.getDeactivatedResolutionStrategies().get().contains(definition)) {
                if (definition.defaultStrategy == HIGHEST_VERSION) {
                    selectHighestVersion(details);
                } else if (definition.defaultStrategy == FIRST_MODULE) {
                    select(details, definition.modules.get(0));
                }
            }
        });
    }

    private void selectHighestVersion(CapabilityResolutionDetails details) {
        // Do not use 'details.selectHighestVersion()' because it does not stack well when build authors
        // needs to register another resolution strategy to select a specific modules.
        // See: https://github.com/gradle/gradle/issues/20348
        // 'findHighestVersionCandidate()' here acts as a fallback/default strategy.
        // Note: Our implementation uses the versions of the module candidates, while selectHighestVersion() uses the capability's versions.
        ComponentVariantIdentifier candidate = findHighestVersionCandidate(details.getCandidates());
        if (candidate != null) {
            details.select(candidate);
            details.because("latest version of " + details.getCapability().getGroup() + ":" + details.getCapability().getName());
        }
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

    @Nullable
    private ComponentVariantIdentifier findHighestVersionCandidate(List<ComponentVariantIdentifier> candidates) {
        ComponentVariantIdentifier selected = candidates.get(0);
        boolean moreThanOneSelected = false;
        for (int i = 1; i < candidates.size(); i++) {
            ComponentVariantIdentifier next = candidates.get(i);
            if (!(next.getId() instanceof ModuleComponentIdentifier)) {
                return null;
            }
            VersionNumber selectedVersion = VersionNumber.parse(((ModuleComponentIdentifier) selected.getId()).getVersion());
            VersionNumber nextVersion = VersionNumber.parse(((ModuleComponentIdentifier) next.getId()).getVersion());

            if (nextVersion.equals(selectedVersion)) {
                moreThanOneSelected = true;
            } else if (nextVersion.compareTo(selectedVersion) > 0) {
                selected = next;
                moreThanOneSelected = false;
            }
        }
        return moreThanOneSelected ? null : selected;
    }
}
