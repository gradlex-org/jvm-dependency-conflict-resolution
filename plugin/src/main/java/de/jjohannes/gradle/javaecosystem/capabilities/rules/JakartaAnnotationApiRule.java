package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class JakartaAnnotationApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.annotation";
    public static final String CAPABILITY_NAME = "jsr250-api";

    public static final String[] MODULES = {
            "jakarta.annotation:jakarta.annotation-api",
            "javax.annotation:javax.annotation-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
            CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
