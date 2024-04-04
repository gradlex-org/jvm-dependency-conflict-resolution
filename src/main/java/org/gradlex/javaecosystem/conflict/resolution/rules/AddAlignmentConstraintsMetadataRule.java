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

package org.gradlex.javaecosystem.conflict.resolution.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

import javax.inject.Inject;
import java.util.List;

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
                    } else  {
                        c.add(group + ":" + member + ":" + version);
                    }
                }
            });
        });
    }
}
