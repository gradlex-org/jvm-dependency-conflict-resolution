package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class HamcrestRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "org.hamcrest";
    public static final String CAPABILITY_NAME_CORE = "hamcrest-core";
    public static final String CAPABILITY_NAME_LIBRARY = "hamcrest-library";

    public static final String[] MODULES = {
            "org.hamcrest:hamcrest"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> {
            capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME_CORE, version);
            capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME_LIBRARY, version);
        }));
    }
}
