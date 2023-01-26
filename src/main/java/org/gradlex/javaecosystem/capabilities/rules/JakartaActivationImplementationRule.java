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
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

@CacheableRule
@NonNullApi
public abstract class JakartaActivationImplementationRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.sun.activation";
    public static final String CAPABILITY_NAME = "jakarta.activation";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    // Starting with this version the implementation moved to the 'org.eclipse' package and is no longer a conflict
    public static final String FIRST_ECLIPSE_VERSION = "2.0.0";

    public static final String[] MODULES = {
            "org.eclipse.angus:angus-activation"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(FIRST_ECLIPSE_VERSION)) < 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }
}
