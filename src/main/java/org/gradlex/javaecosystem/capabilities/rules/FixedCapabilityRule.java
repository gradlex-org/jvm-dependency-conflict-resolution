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

import javax.inject.Inject;

/**
 * Abstract rule adding a capability with a hard coded version.
 */
@CacheableRule
public abstract class FixedCapabilityRule extends EnumBasedRule {

    @Inject
    public FixedCapabilityRule(CapabilityDefinitions capabilityDefinition) {
        super(capabilityDefinition);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        return "1.0";
    }
}