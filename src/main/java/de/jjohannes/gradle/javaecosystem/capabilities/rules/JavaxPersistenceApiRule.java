package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import de.jjohannes.gradle.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class JavaxPersistenceApiRule implements ComponentMetadataRule {
    public static final String CAPABILITY_GROUP = "javax.persistence";
    public static final String CAPABILITY_NAME = "javax.persistence-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String FIRST_JAKARTA_VERSION = "3.0.0";

    public static final String[] MODULES = {
            "jakarta.persistence:jakarta.persistence-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.2-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.1-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.0-api"
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
