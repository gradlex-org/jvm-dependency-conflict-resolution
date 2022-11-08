package de.jjohannes.gradle.javaecosystem.capabilities;

import de.jjohannes.gradle.javaecosystem.capabilities.rules.*;
import de.jjohannes.gradle.javaecosystem.capabilities.util.VersionNumber;
import de.jjohannes.gradle.javaecosystem.capabilities.componentrules.GuavaComponentRule;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.util.GradleVersion;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
@NonNullApi
public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<ExtensionAware> {

    private static final GradleVersion MINIMUM_SUPPORTED_VERSION = GradleVersion.version("6.0");
    private static final GradleVersion MINIMUM_SUPPORTED_VERSION_SETTINGS = GradleVersion.version("6.8");

    /**
     * Map: Capability -> Default Module to resolve to (or 'null' if resolve to the highest version)
     */
    private final Map<String, String> standardResolutionStrategy = new HashMap<>();

    @Override
    public void apply(ExtensionAware projectOrSettings) {
        if (GradleVersion.current().compareTo(MINIMUM_SUPPORTED_VERSION) < 0) {
            throw new IllegalStateException("Plugin requires at least Gradle " + MINIMUM_SUPPORTED_VERSION.getVersion());
        }

        Set<String> allCapabilities = new TreeSet<>();

        ComponentMetadataHandler components;
        if (projectOrSettings instanceof Project) {
            components = ((Project) projectOrSettings).getDependencies().getComponents();
        } else if (projectOrSettings instanceof Settings) {
            if (GradleVersion.current().compareTo(MINIMUM_SUPPORTED_VERSION_SETTINGS) < 0) {
                throw new IllegalStateException("Using this plugin in settings.gradle requires at least Gradle " + MINIMUM_SUPPORTED_VERSION_SETTINGS.getVersion());
            }
            //noinspection UnstableApiUsage
            components = ((Settings) projectOrSettings).getDependencyResolutionManagement().getComponents();
        } else {
            throw new IllegalStateException("Cannot apply plugin to: " + projectOrSettings.getClass().getName());
        }

        registerCapabilityRules(components, allCapabilities);
        registerComponentRules(components);

        if (projectOrSettings instanceof Project) {
            JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities =
                    projectOrSettings.getExtensions().create("javaEcosystemCapabilities", JavaEcosystemCapabilitiesExtension.class, allCapabilities);
            ((Project) projectOrSettings).getConfigurations().all(configuration ->
                    defineStrategies(javaEcosystemCapabilities, configuration.getResolutionStrategy().getCapabilitiesResolution()));
        }
    }

    private void registerCapabilityRules(ComponentMetadataHandler components, Set<String> allCapabilities) {
        registerRule(AsmRule.CAPABILITY, AsmRule.MODULES, AsmRule.class, null, components, allCapabilities);
        registerRule(C3p0Rule.CAPABILITY, C3p0Rule.MODULES, C3p0Rule.class, null, components, allCapabilities);
        registerRule(CGlibRule.CAPABILITY, CGlibRule.MODULES, CGlibRule.class, null, components, allCapabilities);
        registerRule(CommonsIoRule.CAPABILITY, CommonsIoRule.MODULES, CommonsIoRule.class, null, components, allCapabilities);
        registerRule(Dom4jRule.CAPABILITY, Dom4jRule.MODULES, Dom4jRule.class, null, components, allCapabilities);
        registerRule(GoogleCollectionsRule.CAPABILITY, GoogleCollectionsRule.MODULES, GoogleCollectionsRule.class, null, components, allCapabilities);
        registerRule(GuavaListenableFutureRule.CAPABILITY, GuavaListenableFutureRule.MODULES, GuavaListenableFutureRule.class, null, components, allCapabilities);
        registerRule(GuavaRule.CAPABILITY, GuavaRule.MODULES, GuavaRule.class, null, components, allCapabilities);
        registerRule(HamcrestCoreRule.CAPABILITY, HamcrestCoreRule.MODULES, HamcrestCoreRule.class, HamcrestCoreRule.MODULES[0], components, allCapabilities);
        registerRule(HamcrestLibraryRule.CAPABILITY, HamcrestLibraryRule.MODULES, HamcrestLibraryRule.class, HamcrestLibraryRule.MODULES[0], components, allCapabilities);

        registerRule(JakartaActivationApiRule.CAPABILITY, JakartaActivationApiRule.MODULES, JakartaActivationApiRule.class, null, components, allCapabilities);
        registerRule(JakartaAnnotationApiRule.CAPABILITY, JakartaAnnotationApiRule.MODULES, JakartaAnnotationApiRule.class, null, components, allCapabilities);
        registerRule(JakartaMailApiRule.CAPABILITY, JakartaMailApiRule.MODULES, JakartaMailApiRule.class, null, components, allCapabilities);
        registerRule(JakartaServletApiRule.CAPABILITY, JakartaServletApiRule.MODULES, JakartaServletApiRule.class, null, components, allCapabilities);
        registerRule(JakartaWsRsApiRule.CAPABILITY, JakartaWsRsApiRule.MODULES, JakartaWsRsApiRule.class, null, components, allCapabilities);

        registerRule(JavaAssistRule.CAPABILITY, JavaAssistRule.MODULES, JavaAssistRule.class, null, components, allCapabilities);

        registerRule(JavaxActivationApiRule.CAPABILITY, JavaxActivationApiRule.MODULES, JavaxActivationApiRule.class, null, components, allCapabilities);
        registerRule(JavaxAnnotationApiRule.CAPABILITY, JavaxAnnotationApiRule.MODULES, JavaxAnnotationApiRule.class, null, components, allCapabilities);
        registerRule(JavaxEjbApiRule.CAPABILITY, JavaxEjbApiRule.MODULES, JavaxEjbApiRule.class, null, components, allCapabilities);
        registerRule(JavaxElApiRule.CAPABILITY, JavaxElApiRule.MODULES, JavaxElApiRule.class, null, components, allCapabilities);
        registerRule(JavaxInjectApiRule.CAPABILITY, JavaxInjectApiRule.MODULES, JavaxInjectApiRule.class, null, components, allCapabilities);
        registerRule(JavaxJwsApisRule.CAPABILITY, JavaxJwsApisRule.MODULES, JavaxJwsApisRule.class, null, components, allCapabilities);
        registerRule(JavaxMailApiRule.CAPABILITY, JavaxMailApiRule.MODULES, JavaxMailApiRule.class, null, components, allCapabilities);
        registerRule(JavaxPersistenceApiRule.CAPABILITY, JavaxPersistenceApiRule.MODULES, JavaxPersistenceApiRule.class, null, components, allCapabilities);
        registerRule(JavaxServletApiRule.CAPABILITY, JavaxServletApiRule.MODULES, JavaxServletApiRule.class, null, components, allCapabilities);
        registerRule(JavaxSoapApiRule.CAPABILITY, JavaxSoapApiRule.MODULES, JavaxSoapApiRule.class, null, components, allCapabilities);
        registerRule(JavaxValidationApiRule.CAPABILITY, JavaxValidationApiRule.MODULES, JavaxValidationApiRule.class, null, components, allCapabilities);
        registerRule(JavaxWsRsApiRule.CAPABILITY, JavaxWsRsApiRule.MODULES, JavaxWsRsApiRule.class, null, components, allCapabilities);
        registerRule(JavaxXmlBindApiRule.CAPABILITY, JavaxXmlBindApiRule.MODULES, JavaxXmlBindApiRule.class, null, components, allCapabilities);
        registerRule(JavaxXmlWsApiRule.CAPABILITY, JavaxXmlWsApiRule.MODULES, JavaxXmlWsApiRule.class, null, components, allCapabilities);

        registerRule(JtsCoreRule.CAPABILITY, JtsCoreRule.MODULES, JtsCoreRule.class, null, components, allCapabilities);
        registerRule(JtsRule.CAPABILITY, JtsRule.MODULES, JtsRule.class, null, components, allCapabilities);
        registerRule(JunitRule.CAPABILITY, JunitRule.MODULES, JunitRule.class, null, components, allCapabilities);
        registerRule(PostgresqlRule.CAPABILITY, PostgresqlRule.MODULES, PostgresqlRule.class, null, components, allCapabilities);
        registerRule(StaxApiRule.CAPABILITY, StaxApiRule.MODULES, StaxApiRule.class, null, components, allCapabilities);
        registerRule(VelocityRule.CAPABILITY, VelocityRule.MODULES, VelocityRule.class, null, components, allCapabilities);
        registerRule(WoodstoxAslRule.CAPABILITY, WoodstoxAslRule.MODULES, WoodstoxAslRule.class, null, components, allCapabilities);
        registerRule(WoodstoxLgplRule.CAPABILITY, WoodstoxLgplRule.MODULES, WoodstoxLgplRule.class, null, components, allCapabilities);
    }

    private void registerComponentRules(ComponentMetadataHandler components) {
        components.withModule(GuavaComponentRule.MODULE, GuavaComponentRule.class);
    }

    private void registerRule(
            String capability,
            String[] modules,
            Class<? extends ComponentMetadataRule> rule,
            @Nullable String resolveToModule,
            ComponentMetadataHandler components,
            Set<String> allCapabilities) {

        allCapabilities.add(capability);
        standardResolutionStrategy.put(capability, resolveToModule);

        for (String module : modules) {
            components.withModule(module, rule);
        }
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
