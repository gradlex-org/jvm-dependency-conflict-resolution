package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class JakartaXmlWsApisRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.xml.ws";
    public static final String CAPABILITY_NAME = "jaxws-api";

    public static final String[] MODULES = {
            "jakarta.xml.ws:jakarta.xml.ws-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
