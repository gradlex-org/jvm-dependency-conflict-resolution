package org.gradlex.javaecosystem.capabilities.rules;

import org.gradlex.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class JakartaMailApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "jakarta.mail";
    public static final String CAPABILITY_NAME = "jakarta.mail-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "com.sun.mail:mailapi",
            "com.sun.mail:jakarta.mail",
            "org.eclipse.angus:jakarta.mail"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version = context.getDetails().getId().getVersion();
        String group = context.getDetails().getId().getGroup();

        if ("org.eclipse.angus".equals(group) // org.eclipse.angus has its own versioning, and everything is Jakarta
                || VersionNumber.parse(version).compareTo(VersionNumber.parse(JavaxMailApiRule.FIRST_JAKARTA_VERSION)) >= 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }
}
