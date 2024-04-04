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

package org.gradlex.javaecosystem.conflict.resolution;

import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradlex.javaecosystem.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddApiDependencyMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddCapabilityMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddCompileOnlyApiDependencyMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddFeatureMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddRuntimeOnlyDependencyMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.AddTargetPlatformVariantsMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.ComponentStatusRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.ReduceToCompileOnlyApiDependencyMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.ReduceToRuntimeOnlyDependencyMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.RemoveCapabilityMetadataRule;
import org.gradlex.javaecosystem.conflict.resolution.rules.RemoveDependencyMetadataRule;

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

    public void addCapability(CapabilityDefinition capability) {
        addCapability(capability.getCapability());
    }

    public void addCapability(String capability) {
        getDependencies().getComponents().withModule(module, AddCapabilityMetadataRule.class, r -> r.params(capability));
    }

    public void removeCapability(CapabilityDefinition capability) {
        removeCapability(capability.getCapability());
    }

    public void removeCapability(String capability) {
        getDependencies().getComponents().withModule(module, RemoveCapabilityMetadataRule.class, r -> r.params(capability));
    }

    public void setStatusToIntegration(String... markerInVersion) {
        getDependencies().getComponents().withModule(module, ComponentStatusRule.class, r -> r.params(Arrays.asList(markerInVersion)));
    }

}
