package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class JakartaXmlApisRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "xml-apis";
    public static final String CAPABILITY_NAME = "xml-apis";

    public static final String[] MODULES = {
            "jakarta.xml.bind:jakarta.xml.bind-api",
            "javax.xml.bind:jaxb-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
