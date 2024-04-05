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

package org.gradlex.jvm.dependency.conflict.resolution;

import org.gradle.api.artifacts.dsl.DependencyHandler;
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

import javax.inject.Inject;
import java.util.Arrays;

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
        getDependencies().getComponents().withModule(module, AddApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Add a dependency in 'runtimeOnly' scope (visible at runtime).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addRuntimeOnlyDependency(String dependency) {
        getDependencies().getComponents().withModule(module, AddRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Add a dependency in 'compileOnlyApi' scope (visible at compile time).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void addCompileOnlyApiDependency(String dependency) {
        getDependencies().getComponents().withModule(module, AddCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Remove the given dependency from all scopes.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void removeDependency(String dependency) {
        getDependencies().getComponents().withModule(module, RemoveDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Reduce the given 'api' dependency to 'runtimeOnly' scope.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void reduceToRuntimeOnlyDependency(String dependency) {
        getDependencies().getComponents().withModule(module, ReduceToRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency));
    }

    /**
     * Reduce the given 'api' dependency to 'compileOnlyApi' scope.
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">component_metadata_rules.html#fixing_wrong_dependency_details</a>
     */
    public void reduceToCompileOnlyApiDependency(String dependency) {
        getDependencies().getComponents().withModule(module, ReduceToCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency));
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
        getDependencies().getComponents().withModule(module, AddCapabilityMetadataRule.class, r -> r.params(capability));
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
        getDependencies().getComponents().withModule(module, RemoveCapabilityMetadataRule.class, r -> r.params(capability));
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
        getDependencies().getComponents().withModule(module, AddTargetPlatformVariantsMetadataRule.class, r -> r.params(classifier, operatingSystem, architecture));
    }

    /**
     * Set the status of pre-release versions that are identified by one of the _marker string_ (e.g. '-rc', '-m') to
     * 'integration' (will then not be considered when using 'latest.release' as version).
     * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#sec:custom_status_scheme">component_metadata_rules.html#sec:custom_status_scheme</a>
     */
    public void setStatusToIntegration(String... markerInVersion) {
        getDependencies().getComponents().withModule(module, ComponentStatusRule.class, r -> r.params(Arrays.asList(markerInVersion)));
    }

}
