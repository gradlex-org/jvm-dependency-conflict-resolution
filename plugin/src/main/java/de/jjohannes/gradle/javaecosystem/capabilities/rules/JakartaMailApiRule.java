package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class JakartaMailApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.mail";
    public static final String CAPABILITY_NAME = "mail";

    public static final String[] MODULES = {
            "com.sun.mail:javax.mail",
            "com.sun.mail:jakarta.mail",
            "com.sun.mail:mailapi"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
