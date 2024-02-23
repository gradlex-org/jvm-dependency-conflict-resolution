package org.gradlex.javaecosystem.capabilities.rules.logging;

import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;

public class Slf4JAlignment implements ComponentMetadataRule {
    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        if (details.getId().getGroup().startsWith("org.slf4j")) {
            details.belongsTo("dev.jacomet.logging.align:slf4j:" + details.getId().getVersion());
        }
    }
}
