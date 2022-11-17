package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class GuavaRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.google.guava";
    public static final String CAPABILITY_NAME = "guava";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "com.google.guava:guava-jdk5"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = parseGuavaVersion(context.getDetails());
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }

    static String parseGuavaVersion(ComponentMetadataDetails details) {
        String versionString = details.getId().getVersion();
        if (!versionString.contains("-")) {
            return versionString;
        }
        return versionString.substring(0, versionString.indexOf("-"));
    }
}
