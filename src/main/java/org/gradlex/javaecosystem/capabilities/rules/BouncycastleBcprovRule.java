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
public abstract class BouncycastleBcprovRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "org.bouncycastle";
    public static final String CAPABILITY_NAME = "bcprov";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "org.bouncycastle:bcprov-debug-jdk14",
            "org.bouncycastle:bcprov-debug-jdk15on",
            "org.bouncycastle:bcprov-debug-jdk15to18",
            "org.bouncycastle:bcprov-debug-jdk18on",
            "org.bouncycastle:bcprov-ext-debug-jdk14",
            "org.bouncycastle:bcprov-ext-debug-jdk15on",
            "org.bouncycastle:bcprov-ext-debug-jdk15to18",
            "org.bouncycastle:bcprov-ext-debug-jdk18on",
            "org.bouncycastle:bcprov-ext-jdk14",
            "org.bouncycastle:bcprov-ext-jdk15",
            "org.bouncycastle:bcprov-ext-jdk15on",
            "org.bouncycastle:bcprov-ext-jdk15to18",
            "org.bouncycastle:bcprov-ext-jdk16",
            "org.bouncycastle:bcprov-ext-jdk18on",
            "org.bouncycastle:bcprov-jdk12",
            "org.bouncycastle:bcprov-jdk14",
            "org.bouncycastle:bcprov-jdk15",
            "org.bouncycastle:bcprov-jdk15+",
            "org.bouncycastle:bcprov-jdk15on",
            "org.bouncycastle:bcprov-jdk15to18",
            "org.bouncycastle:bcprov-jdk16",
            "org.bouncycastle:bcprov-jdk18on",
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
