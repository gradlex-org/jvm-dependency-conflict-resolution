// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.rules.bouncycastle;

import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinitionRule;

@CacheableRule
public abstract class BouncyCastleRule extends CapabilityDefinitionRule {

    @Inject
    public BouncyCastleRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        // The 2.x LTS versions correspond to the 1.x non-lts versions
        String version = id.getVersion();
        if (id.getName().contains("-lts") && version.startsWith("2.")) {
            return "1." + version.substring(2);
        }
        if (!version.contains(".")) {
            // Weird version: bcprov-jdk12:130
            return "1.0";
        }
        return super.getVersion(id);
    }
}
