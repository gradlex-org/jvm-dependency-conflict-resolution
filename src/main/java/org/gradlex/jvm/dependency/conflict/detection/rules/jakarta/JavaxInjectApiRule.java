// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

@CacheableRule
public abstract class JavaxInjectApiRule extends CapabilityDefinitionRule {

    public static final String FIRST_JAKARTA_VERSION = "2.0.0";

    @Inject
    public JavaxInjectApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(getVersion(id)).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0;
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        if ("org.glassfish.hk2.external".equals(id.getGroup())) {
            return "1";
        }
        return id.getVersion();
    }
}
