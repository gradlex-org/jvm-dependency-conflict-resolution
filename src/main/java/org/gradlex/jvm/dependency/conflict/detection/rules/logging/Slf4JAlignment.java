// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.logging;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinitionRule;
import org.gradlex.jvm.dependency.conflict.detection.util.VersionNumber;
import org.jspecify.annotations.NullMarked;

@NullMarked
@CacheableRule
public abstract class Slf4JAlignment extends AlignmentDefinitionRule {

    @Inject
    public Slf4JAlignment(AlignmentDefinition definition) {
        super(definition);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return VersionNumber.parse(id.getVersion()).compareTo(VersionNumber.parse("2.0.8")) < 0;
    }
}
