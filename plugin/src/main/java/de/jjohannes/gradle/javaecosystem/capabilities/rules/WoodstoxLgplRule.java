package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class WoodstoxLgplRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "woodstox";
    public static final String CAPABILITY_NAME = "wstx-lgpl";

    public static final String[] MODULES = {
            "org.codehaus.woodstox:wstx-lgpl",
            "org.codehaus.woodstox:woodstox-core-lgpl"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}

