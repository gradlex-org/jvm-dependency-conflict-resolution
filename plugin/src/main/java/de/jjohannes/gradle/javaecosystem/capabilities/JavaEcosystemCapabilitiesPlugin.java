package de.jjohannes.gradle.javaecosystem.capabilities;

import de.jjohannes.gradle.javaecosystem.capabilities.rules.*;
import de.jjohannes.gradle.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@NonNullApi
public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        registerRules(project.getDependencies().getComponents());
        project.getConfigurations().all(configuration -> defineStrategies(configuration.getResolutionStrategy().getCapabilitiesResolution()));
    }

    private void registerRules(ComponentMetadataHandler components) {
        for (String module : AsmRule.MODULES) { components.withModule(module, AsmRule.class); }
        for (String module : C3p0Rule.MODULES) { components.withModule(module, C3p0Rule.class); }
        for (String module : CGlibRule.MODULES) { components.withModule(module, CGlibRule.class); }
        for (String module : CommonsIoRule.MODULES) { components.withModule(module, CommonsIoRule.class); }
        for (String module : Dom4jRule.MODULES) { components.withModule(module, Dom4jRule.class); }
        for (String module : GuavaRule.MODULES) { components.withModule(module, GuavaRule.class); }
        for (String module : HamcrestCoreRule.MODULES) { components.withModule(module, HamcrestCoreRule.class); }
        for (String module : HamcrestLibraryRule.MODULES) { components.withModule(module, HamcrestLibraryRule.class); }
        for (String module : JakartaActivationApiRule.MODULES) { components.withModule(module, JakartaActivationApiRule.class); }
        for (String module : JakartaAnnotationApiRule.MODULES) { components.withModule(module, JakartaAnnotationApiRule.class); }
        for (String module : JakartaEjbApiRule.MODULES) { components.withModule(module, JakartaEjbApiRule.class); }
        for (String module : JakartaElApiRule.MODULES) { components.withModule(module, JakartaElApiRule.class); }
        for (String module : JakartaInjectApiRule.MODULES) { components.withModule(module, JakartaInjectApiRule.class); }
        for (String module : JakartaJwsApisRule.MODULES) { components.withModule(module, JakartaJwsApisRule.class); }
        for (String module : JakartaMailApiRule.MODULES) { components.withModule(module, JakartaMailApiRule.class); }
        for (String module : JakartaPersistenceApiRule.MODULES) { components.withModule(module, JakartaPersistenceApiRule.class); }
        for (String module : JakartaServletApiRule.MODULES) { components.withModule(module, JakartaServletApiRule.class); }
        for (String module : JakartaSoapApisRule.MODULES) { components.withModule(module, JakartaSoapApisRule.class); }
        for (String module : JakartaValidationAPIRule.MODULES) { components.withModule(module, JakartaValidationAPIRule.class); }
        for (String module : JakartaWsRsApisRule.MODULES) { components.withModule(module, JakartaWsRsApisRule.class); }
        for (String module : JakartaXmlBindApisRule.MODULES) { components.withModule(module, JakartaXmlBindApisRule.class); }
        for (String module : JakartaXmlWsApisRule.MODULES) { components.withModule(module, JakartaXmlWsApisRule.class); }
        for (String module : JavaAssistJbossRule.MODULES) { components.withModule(module, JavaAssistJbossRule.class); }
        for (String module : JavaAssistRule.MODULES) { components.withModule(module, JavaAssistRule.class); }
        for (String module : JtsCoreRule.MODULES) { components.withModule(module, JtsCoreRule.class); }
        for (String module : JtsRule.MODULES) { components.withModule(module, JtsRule.class); }
        for (String module : JunitRule.MODULES) { components.withModule(module, JunitRule.class); }
        for (String module : PostgresqlRule.MODULES) { components.withModule(module, PostgresqlRule.class); }
        for (String module : StaxApiRule.MODULES) { components.withModule(module, StaxApiRule.class); }
        for (String module : VelocityRule.MODULES) { components.withModule(module, VelocityRule.class); }
        for (String module : WoodstoxAslRule.MODULES) { components.withModule(module, WoodstoxAslRule.class); }
        for (String module : WoodstoxLgplRule.MODULES) { components.withModule(module, WoodstoxLgplRule.class); }
    }

    private void defineStrategies(CapabilitiesResolution resolution) {
        selectHighestVersion(resolution, AsmRule.CAPABILITY);
        selectHighestVersion(resolution, C3p0Rule.CAPABILITY);
        selectHighestVersion(resolution, CGlibRule.CAPABILITY);
        selectHighestVersion(resolution, CommonsIoRule.CAPABILITY);
        selectHighestVersion(resolution, Dom4jRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaActivationApiRule.CAPABILITY);
        selectHighestVersion(resolution, GuavaRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaAnnotationApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaEjbApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaElApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaInjectApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaJwsApisRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaMailApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaPersistenceApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaServletApiRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaSoapApisRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaValidationAPIRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaWsRsApisRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaXmlBindApisRule.CAPABILITY);
        selectHighestVersion(resolution, JakartaXmlWsApisRule.CAPABILITY);
        selectHighestVersion(resolution, JavaAssistJbossRule.CAPABILITY);
        selectHighestVersion(resolution, JavaAssistRule.CAPABILITY);
        selectHighestVersion(resolution, JtsCoreRule.CAPABILITY);
        selectHighestVersion(resolution, JtsRule.CAPABILITY);
        selectHighestVersion(resolution, JunitRule.CAPABILITY);
        selectHighestVersion(resolution, PostgresqlRule.CAPABILITY);
        selectHighestVersion(resolution, StaxApiRule.CAPABILITY);
        selectHighestVersion(resolution, VelocityRule.CAPABILITY);
        selectHighestVersion(resolution, WoodstoxAslRule.CAPABILITY);
        selectHighestVersion(resolution, WoodstoxLgplRule.CAPABILITY);

        select(resolution, HamcrestCoreRule.CAPABILITY, HamcrestCoreRule.MODULES);
        select(resolution, HamcrestLibraryRule.CAPABILITY, HamcrestLibraryRule.MODULES);
    }

    private void selectHighestVersion(CapabilitiesResolution resolution, String capability) {
        resolution.withCapability(capability, details -> {
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
        });
    }

    private void select(CapabilitiesResolution resolution, String capability, String[] moduleGAs) {
        resolution.withCapability(capability, details -> {
            for (String moduleGA : moduleGAs) {
                Optional<ComponentVariantIdentifier> module = details.getCandidates().stream().filter(c -> {
                    if (c.getId() instanceof ModuleComponentIdentifier) {
                        return ((ModuleComponentIdentifier) c.getId()).getModuleIdentifier().toString().equals(moduleGA);
                    }
                    return false;
                }).findFirst();

                if (module.isPresent()) {
                    details.select(module.get());
                    return;
                }
            }
        });
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
