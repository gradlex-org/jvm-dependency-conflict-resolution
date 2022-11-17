/*
 * Copyright 2022 the GradleX team.
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
public abstract class WoodstoxAslRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "woodstox";
    public static final String CAPABILITY_NAME = "wstx-asl";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "org.codehaus.woodstox:wstx-asl",
            "org.codehaus.woodstox:woodstox-core-asl"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}

