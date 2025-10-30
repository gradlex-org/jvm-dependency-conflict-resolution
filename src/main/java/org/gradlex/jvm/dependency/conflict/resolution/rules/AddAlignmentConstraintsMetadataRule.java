// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import java.util.List;
import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

/**
 * See:
 * <a href="https://blog.gradle.org/alignment-with-gradle-module-metadata">blog.gradle.org/alignment-with-gradle-module-metadata</a>
 */
@CacheableRule
public abstract class AddAlignmentConstraintsMetadataRule implements ComponentMetadataRule {

    private final List<String> components;

    @Inject
    public AddAlignmentConstraintsMetadataRule(List<String> components) {
        this.components = components;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        String group = context.getDetails().getId().getGroup();
        context.getDetails().allVariants(v -> {
            v.withDependencyConstraints(c -> {
                for (String member : components) {
                    if (member.contains(":")) {
                        c.add(member + ":" + version);
                    } else {
                        c.add(group + ":" + member + ":" + version);
                    }
                }
            });
        });
    }
}
