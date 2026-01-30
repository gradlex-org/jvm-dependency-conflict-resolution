// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
