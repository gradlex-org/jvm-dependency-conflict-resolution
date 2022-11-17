package org.gradlex.javaecosystem.capabilities.rules;

import org.gradlex.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class JavaxMailApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.mail";
    public static final String CAPABILITY_NAME = "mail";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String FIRST_JAKARTA_VERSION = "2.0.0";

    public static final String[] MODULES = {
            // API only
            "com.sun.mail:mailapi",
            "jakarta.mail:jakarta.mail-api",
            // API + Implementation
            "com.sun.mail:javax.mail",
            "com.sun.mail:jakarta.mail"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }
}
