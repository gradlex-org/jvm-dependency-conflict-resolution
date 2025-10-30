// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.logging;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;

@CacheableRule
public abstract class Log4J2Alignment extends AlignmentDefinitionRule {

    @Inject
    public Log4J2Alignment(AlignmentDefinition definition) {
        super(definition);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(id.getVersion()).compareTo(VersionNumber.parse("2.0")) >= 0;
    }
}
