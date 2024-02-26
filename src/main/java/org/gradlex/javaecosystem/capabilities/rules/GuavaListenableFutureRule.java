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

package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.VariantMetadata;

import javax.inject.Inject;

@CacheableRule
public abstract class GuavaListenableFutureRule extends CapabilityDefinitionsRule {

    @Inject
    public GuavaListenableFutureRule(CapabilityDefinitions rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        return "1.0";
    }

    @Override
    protected void additionalAdjustments(VariantMetadata variant) {
        // Despite publishing Gradle Metadata for Guava 32.1+, this part was not adopted eventually
        // See: https://github.com/google/guava/issues/6642#issuecomment-1656201382
        // Remove workaround dependency to '9999.0-empty-to-avoid-conflict-with-guava'
        variant.withDependencies(dependencies -> dependencies.removeIf(d -> "listenablefuture".equals(d.getName())));
    }
}
