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

package org.gradlex.jvm.dependency.conflict.resolution.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

import javax.inject.Inject;

/**
 * See:
 * <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#fixing_wrong_dependency_details">
 *     component_metadata_rules.html#fixing_wrong_dependency_details</a>
 */
@CacheableRule
public abstract class AddApiDependencyMetadataRule implements ComponentMetadataRule {

    private final String dependency;

    @Inject
    public AddApiDependencyMetadataRule(String dependency) {
        this.dependency = dependency;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        context.getDetails().allVariants(v -> v.withDependencies(d -> d.add(dependency)));
    }
}
