// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import java.util.Arrays;
import java.util.Optional;
import javax.inject.Inject;
import org.gradle.api.artifacts.CapabilityResolutionDetails;
import org.gradle.api.artifacts.ComponentVariantIdentifier;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.provider.SetProperty;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class ConflictResolution {

    @Inject
    protected abstract ConfigurationContainer getConfigurations();

    abstract SetProperty<CapabilityDefinition> getDeactivatedResolutionStrategies();

    public void deactivateResolutionStrategy(CapabilityDefinition capability) {
        getDeactivatedResolutionStrategies().add(capability);
    }

    public void deactivateResolutionStrategy(String capability) {
        Optional<CapabilityDefinition> definition = Arrays.stream(CapabilityDefinition.values())
                .filter(c -> capability.equals(c.getCapability()))
                .findFirst();
        definition.ifPresent(c -> getDeactivatedResolutionStrategies().add(c));
    }

    public void selectHighestVersion(CapabilityDefinition capability) {
        deactivateResolutionStrategy(capability);
        doSelectHighestVersion(capability.getCapability());
    }

    public void selectHighestVersion(String capability) {
        deactivateResolutionStrategy(capability);
        doSelectHighestVersion(capability);
    }

    public void select(CapabilityDefinition capability, String module) {
        deactivateResolutionStrategy(capability);
        doSelect(capability.getCapability(), module, false);
    }

    public void select(String capability, String module) {
        deactivateResolutionStrategy(capability);
        doSelect(capability, module, false);
    }

    public void selectLenient(CapabilityDefinition capability, String module) {
        deactivateResolutionStrategy(capability);
        doSelect(capability.getCapability(), module, true);
    }

    private void selectLenient(String capability, String module) {
        deactivateResolutionStrategy(capability);
        doSelect(capability, module, true);
    }

    private void doSelectHighestVersion(String capability) {
        getConfigurations().configureEach(conf -> conf.getResolutionStrategy()
                .getCapabilitiesResolution()
                .withCapability(capability, CapabilityResolutionDetails::selectHighestVersion));
    }

    private void doSelect(String capability, String module, boolean lenient) {
        String group = module.split(":")[0];
        String name = module.split(":")[1];
        getConfigurations().configureEach(conf -> conf.getResolutionStrategy()
                .getCapabilitiesResolution()
                .withCapability(capability, c -> {
                    for (ComponentVariantIdentifier candidate : c.getCandidates()) {
                        ComponentIdentifier id = candidate.getId();
                        if (id instanceof ModuleComponentIdentifier
                                && ((ModuleComponentIdentifier) id).getGroup().equals(group)
                                && ((ModuleComponentIdentifier) id).getModule().equals(name)) {
                            c.select(candidate);
                            return;
                        }
                    }
                    if (lenient && !c.getCandidates().isEmpty()) {
                        c.select(c.getCandidates().get(0));
                    }
                }));
    }
}
