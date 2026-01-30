// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import static org.gradlex.jvm.dependency.conflict.detection.JvmDependencyConflictDetectionPlugin.MINIMUM_SUPPORTED_VERSION_DEPENDENCY_CAPABILITY;

import java.util.Arrays;
import javax.inject.Inject;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.util.GradleVersion;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddApiDependencyMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddCapabilityMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddCompileOnlyApiDependencyMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddFeatureMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddRuntimeOnlyDependencyMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.AddTargetPlatformVariantsMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.ComponentStatusRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.ReduceToCompileOnlyApiDependencyMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.ReduceToRuntimeOnlyDependencyMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.RemoveCapabilityMetadataRule;
import org.gradlex.jvm.dependency.conflict.resolution.rules.RemoveDependencyMetadataRule;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class PatchModule {

    @Inject
    protected abstract DependencyHandler getDependencies();

    private final String module;

    @Inject
    public PatchModule(String module) {
        this.module = module;
    }

    /**
     * Add a dependency in 'api' scope (visible at runtime and compile time).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addApiDependency(String dependency) {
        addApiDependency(dependency, "");
    }

    /**
     * Add a dependency in 'api' scope (visible at runtime and compile time).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addApiDependency(String dependency, String capability) {
        checkGradleVersion(capability);
        getDependencies()
                .getComponents()
                .withModule(module, AddApiDependencyMetadataRule.class, r -> r.params(dependency, capability));
    }

    /**
     * Add a dependency in 'runtimeOnly' scope (visible at runtime).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addRuntimeOnlyDependency(String dependency) {
        addRuntimeOnlyDependency(dependency, "");
    }

    /**
     * Add a dependency in 'runtimeOnly' scope (visible at runtime).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addRuntimeOnlyDependency(String dependency, String capability) {
        checkGradleVersion(capability);
        getDependencies()
                .getComponents()
                .withModule(module, AddRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency, capability));
    }

    /**
     * Add a dependency in 'compileOnlyApi' scope (visible at compile time).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addCompileOnlyApiDependency(String dependency) {
        addCompileOnlyApiDependency(dependency, "");
    }

    /**
     * Add a dependency in 'compileOnlyApi' scope (visible at compile time).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addCompileOnlyApiDependency(String dependency, String capability) {
        checkGradleVersion(capability);
        getDependencies()
                .getComponents()
                .withModule(
                        module, AddCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency, capability));
    }

    /**
     * Remove the given dependency from all scopes.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void removeDependency(String dependency) {
        getDependencies()
                .getComponents()
                .withModule(module, RemoveDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Reduce the given 'api' dependency to 'runtimeOnly' scope.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void reduceToRuntimeOnlyDependency(String dependency) {
        getDependencies()
                .getComponents()
                .withModule(module, ReduceToRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Reduce the given 'api' dependency to 'compileOnlyApi' scope.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void reduceToCompileOnlyApiDependency(String dependency) {
        getDependencies()
                .getComponents()
                .withModule(module, ReduceToCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Add a capability.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>
     * See: <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
     */
    public void addCapability(CapabilityDefinition capability) {
        addCapability(capability.getCapability());
    }

    /**
     * Add a capability.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>
     * See: <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
     */
    public void addCapability(String capability) {
        getDependencies()
                .getComponents()
                .withModule(module, AddCapabilityMetadataRule.class, r -> r.params(capability));
    }

    /**
     * Remove a capability.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>
     * See: <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
     */
    public void removeCapability(CapabilityDefinition capability) {
        removeCapability(capability.getCapability());
    }

    /**
     * Remove a capability.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts">component_metadata_rules.html#adding_missing_capabilities_to_detect_conflicts</a>
     * See: <a href="https://blog.gradle.org/addressing-logging-complexity-capabilities">blog.gradle.org/addressing-logging-complexity-capabilities</a>
     */
    public void removeCapability(String capability) {
        getDependencies()
                .getComponents()
                .withModule(module, RemoveCapabilityMetadataRule.class, r -> r.params(capability));
    }

    /**
     * Make the Jar with the give 'classifier' known as _Feature Variant_ so that it can be selected via capability
     * in a dependency declaration.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities">component_metadata_rules.html#making_different_flavors_of_a_library_available_through_capabilities</a>,
     * See: <a href="https://blog.gradle.org/optional-dependencies">blog.gradle.org/optional-dependencies</a>
     */
    public void addFeature(String classifier) {
        getDependencies().getComponents().withModule(module, AddFeatureMetadataRule.class, r -> r.params(classifier));
    }

    /**
     * Make the Jar with the give 'classifier' known as additional variant with the
     * OperatingSystemFamily and MachineArchitecture attributes set.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_variants_for_native_jars">component_metadata_rules.html#adding_variants_for_native_jars</a>
     */
    public void addTargetPlatformVariant(String classifier, String operatingSystem, String architecture) {
        getDependencies()
                .getComponents()
                .withModule(
                        module,
                        AddTargetPlatformVariantsMetadataRule.class,
                        r -> r.params("", classifier, operatingSystem, architecture));
    }

    /**
     * Make the Jar with the give 'classifier' known as additional variant with the
     * OperatingSystemFamily and MachineArchitecture attributes set.
     * A 'feature' (aka Capability) can optionally be defined to require the variant to be addressed by it.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#adding_variants_for_native_jars">component_metadata_rules.html#adding_variants_for_native_jars</a>
     */
    public void addTargetPlatformVariant(
            String feature, String classifier, String operatingSystem, String architecture) {
        getDependencies()
                .getComponents()
                .withModule(
                        module,
                        AddTargetPlatformVariantsMetadataRule.class,
                        r -> r.params(feature, classifier, operatingSystem, architecture));
    }

    /**
     * Set the status of pre-release versions that are identified by one of the _marker string_ (e.g. '-rc', '-m') to
     * 'integration' (will then not be considered when using 'latest.release' as version).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#sec:custom_status_scheme">component_metadata_rules.html#sec:custom_status_scheme</a>
     */
    public void setStatusToIntegration(String... markerInVersion) {
        getDependencies()
                .getComponents()
                .withModule(module, ComponentStatusRule.class, r -> r.params(Arrays.asList(markerInVersion)));
    }

    private void checkGradleVersion(String capability) {
        if (!capability.isEmpty()
                && GradleVersion.current().compareTo(MINIMUM_SUPPORTED_VERSION_DEPENDENCY_CAPABILITY) < 0) {
            throw new IllegalStateException(
                    "Using add(Api|RuntimeOnly|CompileOnlyApi)Dependency with capability requires at least Gradle "
                            + MINIMUM_SUPPORTED_VERSION_DEPENDENCY_CAPABILITY.getVersion());
        }
    }
}
