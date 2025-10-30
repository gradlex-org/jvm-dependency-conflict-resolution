// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.jakarta;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

@CacheableRule
public abstract class JakartaMailApiRule extends CapabilityDefinitionRule {

    @Inject
    public JakartaMailApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        // org.eclipse.angus has its own versioning, and everything is Jakarta
        return "org.eclipse.angus".equals(id.getGroup())
                || VersionNumber.parse(id.getVersion())
                                .compareTo(VersionNumber.parse(JavaxMailApiRule.FIRST_JAKARTA_VERSION))
                        >= 0;
    }
}
