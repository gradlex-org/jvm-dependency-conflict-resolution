// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
