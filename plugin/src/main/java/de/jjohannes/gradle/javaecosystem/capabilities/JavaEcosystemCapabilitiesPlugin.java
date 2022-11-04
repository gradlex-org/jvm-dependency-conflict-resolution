package de.jjohannes.gradle.javaecosystem.capabilities;

import de.jjohannes.gradle.javaecosystem.capabilities.rules.*;
import de.jjohannes.gradle.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
@NonNullApi
public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<Project> {

    /**
     * Map: Capability -> Default Module to resolve to (or 'null' if resolve to the highest version)
     */
    private final Map<String, String> standardResolutionStrategy = new HashMap<>();

    @Override
    public void apply(Project project) {
        Set<String> allCapabilities = new TreeSet<>();
        JavaEcosystemCapabilitiesExtension javaEcosystemCapabilities =
                project.getExtensions().create("javaEcosystemCapabilities", JavaEcosystemCapabilitiesExtension.class, allCapabilities);
        
        registerCapabilityRules(project.getDependencies().getComponents(), allCapabilities);
        
        project.getConfigurations().all(configuration ->
                defineStrategies(javaEcosystemCapabilities, configuration.getResolutionStrategy().getCapabilitiesResolution()));
    }

    private void registerCapabilityRules(ComponentMetadataHandler components, Set<String> allCapabilities) {
        registerRule(AsmRule.CAPABILITY, AsmRule.MODULES, AsmRule.class, null, components, allCapabilities);
        registerRule(C3p0Rule.CAPABILITY, C3p0Rule.MODULES, C3p0Rule.class, null, components, allCapabilities);
        registerRule(CGlibRule.CAPABILITY, CGlibRule.MODULES, CGlibRule.class, null, components, allCapabilities);
        registerRule(CommonsIoRule.CAPABILITY, CommonsIoRule.MODULES, CommonsIoRule.class, null, components, allCapabilities);
        registerRule(Dom4jRule.CAPABILITY, Dom4jRule.MODULES, Dom4jRule.class, null, components, allCapabilities);
        registerRule(GuavaRule.CAPABILITY, GuavaRule.MODULES, GuavaRule.class, null, components, allCapabilities);
        registerRule(HamcrestCoreRule.CAPABILITY, HamcrestCoreRule.MODULES, HamcrestCoreRule.class, HamcrestCoreRule.MODULES[0], components, allCapabilities);
        registerRule(HamcrestLibraryRule.CAPABILITY, HamcrestLibraryRule.MODULES, HamcrestLibraryRule.class, HamcrestLibraryRule.MODULES[0], components, allCapabilities);
        registerRule(JakartaActivationApiRule.CAPABILITY, JakartaActivationApiRule.MODULES, JakartaActivationApiRule.class, null, components, allCapabilities);
        registerRule(JakartaAnnotationApiRule.CAPABILITY, JakartaAnnotationApiRule.MODULES, JakartaAnnotationApiRule.class, null, components, allCapabilities);
        registerRule(JakartaEjbApiRule.CAPABILITY, JakartaEjbApiRule.MODULES, JakartaEjbApiRule.class, null, components, allCapabilities);
        registerRule(JakartaElApiRule.CAPABILITY, JakartaElApiRule.MODULES, JakartaElApiRule.class, null, components, allCapabilities);
        registerRule(JakartaInjectApiRule.CAPABILITY, JakartaInjectApiRule.MODULES, JakartaInjectApiRule.class, null, components, allCapabilities);
        registerRule(JakartaJwsApisRule.CAPABILITY, JakartaJwsApisRule.MODULES, JakartaJwsApisRule.class, null, components, allCapabilities);
        registerRule(JakartaMailApiRule.CAPABILITY, JakartaMailApiRule.MODULES, JakartaMailApiRule.class, null, components, allCapabilities);
        registerRule(JakartaPersistenceApiRule.CAPABILITY, JakartaPersistenceApiRule.MODULES, JakartaPersistenceApiRule.class, null, components, allCapabilities);
        registerRule(JakartaServletApiRule.CAPABILITY, JakartaServletApiRule.MODULES, JakartaServletApiRule.class, null, components, allCapabilities);
        registerRule(JakartaSoapApisRule.CAPABILITY, JakartaSoapApisRule.MODULES, JakartaSoapApisRule.class, null, components, allCapabilities);
        registerRule(JakartaValidationAPIRule.CAPABILITY, JakartaValidationAPIRule.MODULES, JakartaValidationAPIRule.class, null, components, allCapabilities);
        registerRule(JakartaWsRsApisRule.CAPABILITY, JakartaWsRsApisRule.MODULES, JakartaWsRsApisRule.class, null, components, allCapabilities);
        registerRule(JakartaXmlBindApisRule.CAPABILITY, JakartaXmlBindApisRule.MODULES, JakartaXmlBindApisRule.class, null, components, allCapabilities);
        registerRule(JakartaXmlWsApisRule.CAPABILITY, JakartaXmlWsApisRule.MODULES, JakartaXmlWsApisRule.class, null, components, allCapabilities);
        registerRule(JavaAssistRule.CAPABILITY, JavaAssistRule.MODULES, JavaAssistRule.class, null, components, allCapabilities);
        registerRule(JtsCoreRule.CAPABILITY, JtsCoreRule.MODULES, JtsCoreRule.class, null, components, allCapabilities);
        registerRule(JtsRule.CAPABILITY, JtsRule.MODULES, JtsRule.class, null, components, allCapabilities);
        registerRule(JunitRule.CAPABILITY, JunitRule.MODULES, JunitRule.class, null, components, allCapabilities);
        registerRule(PostgresqlRule.CAPABILITY, PostgresqlRule.MODULES, PostgresqlRule.class, null, components, allCapabilities);
        registerRule(StaxApiRule.CAPABILITY, StaxApiRule.MODULES, StaxApiRule.class, null, components, allCapabilities);
        registerRule(VelocityRule.CAPABILITY, VelocityRule.MODULES, VelocityRule.class, null, components, allCapabilities);
        registerRule(WoodstoxAslRule.CAPABILITY, WoodstoxAslRule.MODULES, WoodstoxAslRule.class, null, components, allCapabilities);
        registerRule(WoodstoxLgplRule.CAPABILITY, WoodstoxLgplRule.MODULES, WoodstoxLgplRule.class, null, components, allCapabilities);
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
            components.withModule(module, rule); //TODO remove param!
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
