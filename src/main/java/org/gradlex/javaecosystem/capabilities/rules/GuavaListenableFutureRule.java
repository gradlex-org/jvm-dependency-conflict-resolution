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

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class GuavaListenableFutureRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.google.guava";
    public static final String CAPABILITY_NAME = "listenablefuture";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "com.google.guava:guava"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        // Despite publishing Gradle Metadata for Guava 32.1+, this part was not adopted eventually
        // See: https://github.com/google/guava/issues/6642#issuecomment-1656201382
        context.getDetails().allVariants(variant -> {
            // Remove workaround dependency to '9999.0-empty-to-avoid-conflict-with-guava'
            variant.withDependencies(dependencies -> dependencies.removeIf(d -> CAPABILITY_NAME.equals(d.getName())));
            variant.withCapabilities(capabilities -> {
                capabilities.removeCapability(CAPABILITY_GROUP, CAPABILITY_NAME); // remove just in case, as some intermediate 32.1.x version did add the capability
                capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, "1.0");
            });
        });
    }
}
