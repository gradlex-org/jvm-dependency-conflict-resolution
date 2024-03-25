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

package org.gradlex.javaecosystem.capabilities.dsl;

import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradlex.javaecosystem.capabilities.customrules.AddApiDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddCapabilityMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddCompileOnlyApiDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddFeatureMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddRuntimeOnlyDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddTargetPlatformVariantsMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.ComponentStatusRule;
import org.gradlex.javaecosystem.capabilities.customrules.ReduceToCompileOnlyApiDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.ReduceToRuntimeOnlyDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.RemoveDependencyMetadataRule;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions;

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

    public void addFeature(String classifier) {
        getDependencies().getComponents().withModule(module, AddFeatureMetadataRule.class, r -> r.params(classifier));
    }

    public void addTargetPlatformVariant(String classifier, String operatingSystem, String architecture) {
        getDependencies().getComponents().withModule(module, AddTargetPlatformVariantsMetadataRule.class, r -> r.params(classifier, operatingSystem, architecture));
    }

    public void removeDependency(String dependency) {
        getDependencies().getComponents().withModule(module, RemoveDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void addApiDependency(String dependency) {
        getDependencies().getComponents().withModule(module, AddApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void addRuntimeOnlyDependency(String dependency) {
        getDependencies().getComponents().withModule(module, AddRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void addCompileOnlyApiDependency(String dependency) {
        getDependencies().getComponents().withModule(module, AddCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void reduceToRuntimeOnlyDependency(String dependency) {
        getDependencies().getComponents().withModule(module, ReduceToRuntimeOnlyDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void reduceToCompileOnlyApiDependency(String dependency) {
        getDependencies().getComponents().withModule(module, ReduceToCompileOnlyApiDependencyMetadataRule.class, r -> r.params(dependency));
    }

    public void addCapability(String capability) {
        getDependencies().getComponents().withModule(module, AddCapabilityMetadataRule.class, r -> r.params(capability));
    }

    public void removeCapability(String capability) {
        getDependencies().getComponents().withModule(module, RemoveDependencyMetadataRule.class, r -> r.params(capability));
    }

    public void removeCapability(CapabilityDefinitions capability) {
        removeCapability(capability.getCapability());
    }

    public void setIntegrationStatus(String... markerInVersion) {
        getDependencies().getComponents().withModule(module, ComponentStatusRule.class, r -> r.params(Arrays.asList(markerInVersion)));
    }

}
