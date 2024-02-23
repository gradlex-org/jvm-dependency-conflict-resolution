/*
 * Copyright 2019 the original author or authors.
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
package org.gradlex.javaecosystem.capabilities.rules.logging;

import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

/**
 * Abstract rule adding a capability with a hard coded version.
 */
abstract class FixedCapabilityRule implements ComponentMetadataRule {
    static final String CAPABILITY_GROUP = "org.gradlex.logging";
    
    private final String name;

    protected FixedCapabilityRule(String name) {
        this.name = name;
    }

    @Override
    public void execute(ComponentMetadataContext context) {
        context.getDetails().allVariants(variant -> {
            variant.withCapabilities(capabilities -> {
                capabilities.addCapability(CAPABILITY_GROUP, name, "1.0");
            });
        });
    }
}