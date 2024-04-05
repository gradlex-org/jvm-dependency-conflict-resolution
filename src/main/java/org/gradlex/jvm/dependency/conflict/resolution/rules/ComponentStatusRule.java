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
import java.util.List;

/**
 * Sets status of component versions that are not final releases to 'integration' instead of 'release'.
 * Otherwise, they are considered when asking for the 'latest.release' version.
 * POM metadata does not support the 'status' concept and thus Gradle assumes everything is a 'release' by default.
 * See: <a href="https://docs.gradle.org/current/userguide/component_metadata_rules.html#sec:custom_status_scheme">component_metadata_rules.html#sec:custom_status_scheme</a>
 */
@CacheableRule
public abstract class ComponentStatusRule implements ComponentMetadataRule {

    private final List<String> integrationVersionMarker;

    @Inject
    public ComponentStatusRule(List<String> integrationVersionMarker) {
        this.integrationVersionMarker = integrationVersionMarker;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion().toLowerCase();
        if (integrationVersionMarker.stream().anyMatch(version::contains)) {
            context.getDetails().setStatus("integration");
        }
    }
}
