package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

import static de.jjohannes.gradle.javaecosystem.capabilities.rules.GuavaRule.parseGuavaVersion;

@CacheableRule
@NonNullApi
public abstract class GoogleCollectionsRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.google.collections";
    public static final String CAPABILITY_NAME = "google-collections";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "com.google.guava:guava"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = parseGuavaVersion(context.getDetails());
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
