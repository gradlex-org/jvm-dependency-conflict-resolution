package org.gradlex.javaecosystem.capabilities.rules;

import org.gradlex.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
@NonNullApi
public abstract class JakartaServletApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "jakarta.servlet";
    public static final String CAPABILITY_NAME = "jakarta.servlet-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String[] MODULES = {
            "org.apache.tomcat:tomcat-servlet-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version;
        if ("org.apache.tomcat".equals(context.getDetails().getId().getGroup())) {
            version = JavaxServletApiRule.servletApiVersionForTomcatVersion(VersionNumber.parse(context.getDetails().getId().getVersion()));
        } else {
            version = context.getDetails().getId().getVersion();
        }

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(JavaxServletApiRule.FIRST_JAKARTA_VERSION)) >= 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }
}
