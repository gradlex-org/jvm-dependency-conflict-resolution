package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class StaxApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "stax";
    public static final String CAPABILITY_NAME = "stax-api";

    public static final String[] MODULES = {
            "org.codehaus.woodstox:stax2-api",
            "javax.xml.stream:stax-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
