package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class DnsRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.amazonaws";
    public static final String CAPABILITY_NAME = "elasticache-java-cluster-client";

    public static final String[] MODULES = {
            "lib:AmazonElastiCacheClusterClient"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
                CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }
}
