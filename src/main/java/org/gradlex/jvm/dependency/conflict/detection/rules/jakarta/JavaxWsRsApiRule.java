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
public abstract class JavaxWsRsApiRule extends CapabilityDefinitionRule {

    static final String FIRST_JAKARTA_VERSION = "3.0.0";

    @Inject
    public JavaxWsRsApiRule(CapabilityDefinition rule) {
        super(rule);
    }

    @Override
    protected boolean shouldApply(ModuleVersionIdentifier id) {
        return "org.jboss.resteasy".equals(id.getGroup())
                || VersionNumber.parse(getVersion(id)).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0;
    }

    @Override
    protected String getVersion(ModuleVersionIdentifier id) {
        String name = id.getName();
        String moduleVersion = id.getVersion();

        if (name.contains("jboss-jaxrs-api_")) {
            return rsApiVersionForJbossName(name);
        }
        if (moduleVersion.endsWith(".Final")) {
            return moduleVersion.substring(0, moduleVersion.indexOf(".Final"));
        }
        if (moduleVersion.endsWith(".GA")) {
            return moduleVersion.substring(0, moduleVersion.indexOf(".GA"));
        }

        return moduleVersion;
    }

    private static String rsApiVersionForJbossName(String name) {
        int index = "jboss-jaxrs-api_".length();
        return name.substring(index, index + 3) + ".0";
    }
}
