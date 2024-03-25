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

import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.model.ObjectFactory;
import org.gradlex.javaecosystem.capabilities.customrules.AddAlignmentConstraintsMetadataRule;
import org.gradlex.javaecosystem.capabilities.customrules.AddBomDependencyMetadataRule;

import javax.inject.Inject;
import java.util.Arrays;

public abstract class Patch {

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract DependencyHandler getDependencies();

    public void module(String module, Action<PatchModule> action) {
        action.execute(getObjects().newInstance(PatchModule.class, module));
    }

    public void alignWithBom(String bom, String... modules) {
        for (String module : modules) {
            getDependencies().getComponents().withModule(module, AddBomDependencyMetadataRule.class, r -> r.params(bom));
        }
    }

    public void align(String... modules) {
        for (String module : modules) {
            getDependencies().getComponents().withModule(module, AddAlignmentConstraintsMetadataRule.class, r -> r.params(Arrays.asList(modules)));
        }
    }
}