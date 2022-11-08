package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class JakartaWsRsApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "jakarta.ws.rs";
    public static final String CAPABILITY_NAME = "jakarta.ws.rs-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_3.0_spec"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, "3.0")
        ));
    }
}
