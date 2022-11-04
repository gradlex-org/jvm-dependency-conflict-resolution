package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class GuavaListenableFutureRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "com.google.guava";
    public static final String CAPABILITY_NAME = "listenablefuture";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "com.google.guava:guava"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        context.getDetails().allVariants(variant -> {
            // Remove workaround dependency to '9999.0-empty-to-avoid-conflict-with-guava'
            variant.withDependencies(dependencies -> dependencies.removeIf(d -> CAPABILITY_NAME.equals(d.getName())));
            variant.withCapabilities(capabilities -> capabilities.addCapability(
                    CAPABILITY_GROUP, CAPABILITY_NAME, "1.0"
            ));
        });
    }
}
