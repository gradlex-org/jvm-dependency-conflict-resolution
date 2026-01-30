// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.gradle.api.artifacts.DirectDependencyMetadata;
import org.gradle.api.artifacts.capability.CapabilitySelector;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.internal.artifacts.capability.DefaultSpecificCapabilitySelector;
import org.gradle.api.internal.artifacts.repositories.resolver.AbstractDependencyMetadataAdapter;
import org.gradle.api.internal.artifacts.repositories.resolver.DirectDependencyMetadataAdapter;
import org.gradle.internal.component.external.model.DefaultImmutableCapability;
import org.gradle.internal.component.external.model.DefaultModuleComponentSelector;
import org.gradle.internal.component.external.model.ModuleDependencyMetadata;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DependencyModification {

    static void addCapability(DirectDependencyMetadata dependency, String capability) {
        if (dependency instanceof DirectDependencyMetadataAdapter) {
            doAddCapability((DirectDependencyMetadataAdapter) dependency, capability);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void doAddCapability(DirectDependencyMetadataAdapter dependency, String capability) {
        String[] split = capability.split(":");
        String group = split[0];
        String name = split[1];
        ModuleComponentSelector selector = dependency.getMetadata().getSelector();
        Set<CapabilitySelector> selectors = new HashSet<>(selector.getCapabilitySelectors());
        selectors.add(new DefaultSpecificCapabilitySelector(new DefaultImmutableCapability(group, name, null)));
        ModuleComponentSelector target = DefaultModuleComponentSelector.newSelector(
                selector.getModuleIdentifier(), selector.getVersionConstraint(), selector.getAttributes(), selectors);
        ModuleDependencyMetadata metadata =
                (ModuleDependencyMetadata) dependency.getMetadata().withTarget(target);
        updateMetadata(dependency, metadata);
    }

    private static void updateMetadata(DirectDependencyMetadata dependency, ModuleDependencyMetadata metadata) {
        try {
            Method updateMetadata = AbstractDependencyMetadataAdapter.class.getDeclaredMethod(
                    "updateMetadata", ModuleDependencyMetadata.class);
            updateMetadata.setAccessible(true);
            updateMetadata.invoke(dependency, metadata);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
