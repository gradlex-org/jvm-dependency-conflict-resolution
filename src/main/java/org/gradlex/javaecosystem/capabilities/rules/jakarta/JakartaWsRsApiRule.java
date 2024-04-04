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

package org.gradlex.javaecosystem.capabilities.rules.jakarta;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinition;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitionRule;

import javax.inject.Inject;

@CacheableRule
public abstract class JakartaWsRsApiRule extends CapabilityDefinitionRule {

    @Inject
    public JakartaWsRsApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        if ("jakarta.ws.rs".equals(id.getGroup())) {
            return id.getVersion();
        }
        return "3.0.0";
    }
}
