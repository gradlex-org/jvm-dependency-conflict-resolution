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

package org.gradlex.javaecosystem.conflict.detection.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import javax.inject.Inject;

@CacheableRule
public abstract class AlignmentDefinitionRule implements ComponentMetadataRule {

    private final AlignmentDefinition definition;

    @Inject
    public AlignmentDefinitionRule(AlignmentDefinition definition) {
        this.definition = definition;
    }

    @Override
    public final void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        if (shouldApply(details.getId())) {
            String version = details.getId().getVersion();
            details.belongsTo(definition.getBom() + ":" + version, definition.isVirtual());
        }
    }

    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return true;
    }
}
