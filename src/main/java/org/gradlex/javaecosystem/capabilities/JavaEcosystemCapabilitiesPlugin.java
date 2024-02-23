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

package org.gradlex.javaecosystem.capabilities;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.plugins.PluginAware;
import org.gradlex.javaecosystem.capabilities.rules.AopallianceRule;
import org.gradlex.javaecosystem.capabilities.rules.AsmRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcmailRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcpgRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcpkixRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcprovRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBctlsRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBctspRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcutilRule;
import org.gradlex.javaecosystem.capabilities.rules.C3p0Rule;
import org.gradlex.javaecosystem.capabilities.rules.CGlibRule;
import org.gradlex.javaecosystem.capabilities.rules.CommonsIoRule;
import org.gradlex.javaecosystem.capabilities.rules.Dom4jRule;
import org.gradlex.javaecosystem.capabilities.rules.FindbugsAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.GoogleCollectionsRule;
import org.gradlex.javaecosystem.capabilities.rules.GuavaListenableFutureRule;
import org.gradlex.javaecosystem.capabilities.rules.GuavaRule;
import org.gradlex.javaecosystem.capabilities.rules.HamcrestCoreRule;
import org.gradlex.javaecosystem.capabilities.rules.HamcrestLibraryRule;
import org.gradlex.javaecosystem.capabilities.rules.HikariCPRule;
import org.gradlex.javaecosystem.capabilities.rules.IntellijAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaActivationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaActivationImplementationRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaAnnotationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaJsonApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaMailApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaServletApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketClientApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWsRsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaAssistRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxActivationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxAnnotationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxEjbApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxElApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxInjectApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxJsonApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxJwsApisRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxMailApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxPersistenceApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxServletApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxServletJspRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxServletJstlRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxSoapApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxValidationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxWebsocketApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxWsRsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxXmlBindApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxXmlWsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JcipAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.JnaPlatformRule;
import org.gradlex.javaecosystem.capabilities.rules.JtsCoreRule;
import org.gradlex.javaecosystem.capabilities.rules.JtsRule;
import org.gradlex.javaecosystem.capabilities.rules.JunitRule;
import org.gradlex.javaecosystem.capabilities.rules.PostgresqlRule;
import org.gradlex.javaecosystem.capabilities.rules.StaxApiRule;
import org.gradlex.javaecosystem.capabilities.rules.VelocityRule;
import org.gradlex.javaecosystem.capabilities.rules.WoodstoxAslRule;
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
@NonNullApi
public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<PluginAware> {

    /**
     * Map: Capability -> Default Module to resolve to (or 'null' if resolve to the highest version)
     */
    private final Map<String, String> standardResolutionStrategy = new HashMap<>();

    @Override
    public void apply(PluginAware projectOrSettings) {
        projectOrSettings.getPlugins().apply(JavaEcosystemCapabilitiesBasePlugin.class);

        Set<String> allCapabilities = new TreeSet<>();

        registerCapabilityRules(allCapabilities);

        if (projectOrSettings instanceof Project) {
            Project project = (Project) projectOrSettings;
            JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities =
                    project.getExtensions().create("javaEcosystemCapabilities", JavaEcosystemCapabilitiesExtension.class, allCapabilities);
            project.getConfigurations().all(configuration ->
                    defineStrategies(javaEcosystemCapabilities, configuration.getResolutionStrategy().getCapabilitiesResolution()));
            project.getPlugins().withId("de.jjohannes.missing-metadata-guava", plugin -> {
                project.getLogger().lifecycle("[WARN] Remove 'de.jjohannes.missing-metadata-guava' plugin from the build. The functionality is included in 'org.gradlex.java-ecosystem-capabilities'.");
            });
        }
    }

    private void registerCapabilityRules(Set<String> allCapabilities) {
        configureResultionStrategy(AopallianceRule.CAPABILITY, AopallianceRule.MODULES, AopallianceRule.class, null, allCapabilities);
        configureResultionStrategy(AsmRule.CAPABILITY, AsmRule.MODULES, AsmRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBcmailRule.CAPABILITY, BouncycastleBcmailRule.MODULES, BouncycastleBcmailRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBcpgRule.CAPABILITY, BouncycastleBcpgRule.MODULES, BouncycastleBcpgRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBcpkixRule.CAPABILITY, BouncycastleBcpkixRule.MODULES, BouncycastleBcpkixRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBcprovRule.CAPABILITY, BouncycastleBcprovRule.MODULES, BouncycastleBcprovRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBctlsRule.CAPABILITY, BouncycastleBctlsRule.MODULES, BouncycastleBctlsRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBctspRule.CAPABILITY, BouncycastleBctspRule.MODULES, BouncycastleBctspRule.class, null, allCapabilities);
        configureResultionStrategy(BouncycastleBcutilRule.CAPABILITY, BouncycastleBcutilRule.MODULES, BouncycastleBcutilRule.class, null, allCapabilities);
        configureResultionStrategy(C3p0Rule.CAPABILITY, C3p0Rule.MODULES, C3p0Rule.class, null, allCapabilities);
        configureResultionStrategy(CGlibRule.CAPABILITY, CGlibRule.MODULES, CGlibRule.class, null, allCapabilities);
        configureResultionStrategy(CommonsIoRule.CAPABILITY, CommonsIoRule.MODULES, CommonsIoRule.class, null, allCapabilities);
        configureResultionStrategy(Dom4jRule.CAPABILITY, Dom4jRule.MODULES, Dom4jRule.class, null, allCapabilities);
        configureResultionStrategy(FindbugsAnnotationsRule.CAPABILITY, FindbugsAnnotationsRule.MODULES, FindbugsAnnotationsRule.class, null, allCapabilities);
        configureResultionStrategy(GoogleCollectionsRule.CAPABILITY, GoogleCollectionsRule.MODULES, GoogleCollectionsRule.class, null, allCapabilities);
        configureResultionStrategy(GuavaListenableFutureRule.CAPABILITY, GuavaListenableFutureRule.MODULES, GuavaListenableFutureRule.class, null, allCapabilities);
        configureResultionStrategy(GuavaRule.CAPABILITY, GuavaRule.MODULES, GuavaRule.class, null, allCapabilities);
        configureResultionStrategy(HamcrestCoreRule.CAPABILITY, HamcrestCoreRule.MODULES, HamcrestCoreRule.class, HamcrestCoreRule.MODULES[0], allCapabilities);
        configureResultionStrategy(HamcrestLibraryRule.CAPABILITY, HamcrestLibraryRule.MODULES, HamcrestLibraryRule.class, HamcrestLibraryRule.MODULES[0], allCapabilities);
        configureResultionStrategy(HikariCPRule.CAPABILITY, HikariCPRule.MODULES, HikariCPRule.class, null, allCapabilities);
        configureResultionStrategy(IntellijAnnotationsRule.CAPABILITY, IntellijAnnotationsRule.MODULES, IntellijAnnotationsRule.class, null, allCapabilities);

        configureResultionStrategy(JakartaActivationApiRule.CAPABILITY, JakartaActivationApiRule.MODULES, JakartaActivationApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaActivationImplementationRule.CAPABILITY, JakartaActivationImplementationRule.MODULES, JakartaActivationImplementationRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaAnnotationApiRule.CAPABILITY, JakartaAnnotationApiRule.MODULES, JakartaAnnotationApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaJsonApiRule.CAPABILITY, JakartaJsonApiRule.MODULES, JakartaJsonApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaMailApiRule.CAPABILITY, JakartaMailApiRule.MODULES, JakartaMailApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaServletApiRule.CAPABILITY, JakartaServletApiRule.MODULES, JakartaServletApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaWebsocketApiRule.CAPABILITY, JakartaWebsocketApiRule.MODULES, JakartaWebsocketApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaWebsocketClientApiRule.CAPABILITY, JakartaWebsocketClientApiRule.MODULES, JakartaWebsocketClientApiRule.class, null, allCapabilities);
        configureResultionStrategy(JakartaWsRsApiRule.CAPABILITY, JakartaWsRsApiRule.MODULES, JakartaWsRsApiRule.class, null, allCapabilities);

        configureResultionStrategy(JavaAssistRule.CAPABILITY, JavaAssistRule.MODULES, JavaAssistRule.class, null, allCapabilities);

        configureResultionStrategy(JavaxActivationApiRule.CAPABILITY, JavaxActivationApiRule.MODULES, JavaxActivationApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxAnnotationApiRule.CAPABILITY, JavaxAnnotationApiRule.MODULES, JavaxAnnotationApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxEjbApiRule.CAPABILITY, JavaxEjbApiRule.MODULES, JavaxEjbApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxElApiRule.CAPABILITY, JavaxElApiRule.MODULES, JavaxElApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxInjectApiRule.CAPABILITY, JavaxInjectApiRule.MODULES, JavaxInjectApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxJsonApiRule.CAPABILITY, JavaxJsonApiRule.MODULES, JavaxJsonApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxJwsApisRule.CAPABILITY, JavaxJwsApisRule.MODULES, JavaxJwsApisRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxMailApiRule.CAPABILITY, JavaxMailApiRule.MODULES, JavaxMailApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxPersistenceApiRule.CAPABILITY, JavaxPersistenceApiRule.MODULES, JavaxPersistenceApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxServletApiRule.CAPABILITY, JavaxServletApiRule.MODULES, JavaxServletApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxServletJspRule.CAPABILITY, JavaxServletJspRule.MODULES, JavaxServletJspRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxServletJstlRule.CAPABILITY, JavaxServletJstlRule.MODULES, JavaxServletJstlRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxSoapApiRule.CAPABILITY, JavaxSoapApiRule.MODULES, JavaxSoapApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxValidationApiRule.CAPABILITY, JavaxValidationApiRule.MODULES, JavaxValidationApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxWebsocketApiRule.CAPABILITY, JavaxWebsocketApiRule.MODULES, JavaxWebsocketApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxWsRsApiRule.CAPABILITY, JavaxWsRsApiRule.MODULES, JavaxWsRsApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxXmlBindApiRule.CAPABILITY, JavaxXmlBindApiRule.MODULES, JavaxXmlBindApiRule.class, null, allCapabilities);
        configureResultionStrategy(JavaxXmlWsApiRule.CAPABILITY, JavaxXmlWsApiRule.MODULES, JavaxXmlWsApiRule.class, null, allCapabilities);

        configureResultionStrategy(JcipAnnotationsRule.CAPABILITY, JcipAnnotationsRule.MODULES, JcipAnnotationsRule.class, null, allCapabilities);
        configureResultionStrategy(JnaPlatformRule.CAPABILITY, JnaPlatformRule.MODULES, JnaPlatformRule.class, null, allCapabilities);
        configureResultionStrategy(JtsCoreRule.CAPABILITY, JtsCoreRule.MODULES, JtsCoreRule.class, null, allCapabilities);
        configureResultionStrategy(JtsRule.CAPABILITY, JtsRule.MODULES, JtsRule.class, null, allCapabilities);
        configureResultionStrategy(JunitRule.CAPABILITY, JunitRule.MODULES, JunitRule.class, null, allCapabilities);
        configureResultionStrategy(PostgresqlRule.CAPABILITY, PostgresqlRule.MODULES, PostgresqlRule.class, null, allCapabilities);
        configureResultionStrategy(StaxApiRule.CAPABILITY, StaxApiRule.MODULES, StaxApiRule.class, null, allCapabilities);
        configureResultionStrategy(VelocityRule.CAPABILITY, VelocityRule.MODULES, VelocityRule.class, null, allCapabilities);
        configureResultionStrategy(WoodstoxAslRule.CAPABILITY, WoodstoxAslRule.MODULES, WoodstoxAslRule.class, null, allCapabilities);
    }

    private void configureResultionStrategy(
            String capability,
            String[] modules,
            Class<? extends ComponentMetadataRule> rule,
            @Nullable String resolveToModule,
            Set<String> allCapabilities) {

        allCapabilities.add(capability);
        standardResolutionStrategy.put(capability, resolveToModule);
    }

    private void defineStrategies(JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities, CapabilitiesResolution resolution) {
        for (String capability : javaEcosystemCapabilities.getAllCapabilities()) {
            String strategy = standardResolutionStrategy.get(capability);
            resolution.withCapability(capability, details -> {
                if (!javaEcosystemCapabilities.getDeactivatedResolutionStrategies().get().contains(capability)) {
                    if (strategy == null) {
                        selectHighestVersion(details);
                    } else {
                        select(details, strategy);
                    }
                }
            });
        }
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
