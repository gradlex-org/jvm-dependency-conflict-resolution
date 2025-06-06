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

package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

import javax.inject.Inject;

@CacheableRule
public abstract class JakartaJwsApiRule extends CapabilityDefinitionRule {

    private static final String JWS_MERGER_VERSION = "4.0.0";

    @Inject
    public JakartaJwsApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        if (id.getName().equals("jakarta.xml.ws-api")) {
            return VersionNumber.parse(getVersion(id)).compareTo(VersionNumber.parse(JWS_MERGER_VERSION)) >= 0;
        }
        return true;
    }
}
