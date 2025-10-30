// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

@CacheableRule
public abstract class JavaxJwsApisRule extends CapabilityDefinitionRule {

    static final String FIRST_JAKARTA_VERSION = "3.0.0";

    @Inject
    public JavaxJwsApisRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(getVersion(id)).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0;
    }
}
