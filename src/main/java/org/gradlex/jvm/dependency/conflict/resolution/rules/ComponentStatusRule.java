// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import java.util.List;
import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

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
