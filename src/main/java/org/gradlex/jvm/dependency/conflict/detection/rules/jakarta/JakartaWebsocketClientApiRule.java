// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

@CacheableRule
public abstract class JakartaWebsocketClientApiRule extends CapabilityDefinitionRule {

    @Inject
    public JakartaWebsocketClientApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        if (id.getGroup().startsWith("org.apache.tomcat")) {
            return JavaxWebsocketApiRule.websocketApiVersionForTomcatVersion(VersionNumber.parse(id.getVersion()));
        }
        return id.getVersion();
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(id.getVersion())
                        .compareTo(VersionNumber.parse(JavaxWebsocketApiRule.FIRST_JAKARTA_VERSION))
                >= 0;
    }
}
