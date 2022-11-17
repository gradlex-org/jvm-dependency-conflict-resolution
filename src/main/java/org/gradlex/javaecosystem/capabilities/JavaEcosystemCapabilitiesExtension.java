package org.gradlex.javaecosystem.capabilities;

import org.gradle.api.provider.SetProperty;

import java.util.Set;

public abstract class JavaEcosystemCapabilitiesExtension {

    private final Set<String> allCapabilities;

    public JavaEcosystemCapabilitiesExtension(Set<String> allCapabilities) {
        this.allCapabilities = allCapabilities;
    }

    public Set<String> getAllCapabilities() {
        return allCapabilities;
    }

    public abstract SetProperty<String> getDeactivatedResolutionStrategies();
}
