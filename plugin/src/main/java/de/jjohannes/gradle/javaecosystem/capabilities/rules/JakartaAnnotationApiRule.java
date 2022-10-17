package de.jjohannes.gradle.javaecosystem.capabilities.rules;

import de.jjohannes.gradle.javaecosystem.capabilities.util.VersionNumber;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;

@CacheableRule
public abstract class JakartaAnnotationApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.annotation";
    public static final String CAPABILITY_NAME = "jsr250-api";

    public static final String[] MODULES = {
            "jakarta.annotation:jakarta.annotation-api",
            "javax.annotation:javax.annotation-api",
            "org.apache.tomcat:tomcat-annotations-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version;
        if ("org.apache.tomcat".equals(context.getDetails().getId().getGroup())) {
            version = annotationApiVersionForTomcatVersion(VersionNumber.parse(context.getDetails().getId().getVersion()));
        } else {
            version = context.getDetails().getId().getVersion();
        }
        context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities -> capabilities.addCapability(
            CAPABILITY_GROUP, CAPABILITY_NAME, version
        )));
    }

    // This is probably 100% accurate - older Tomcat versions might ship older 1.x specs
    private static String annotationApiVersionForTomcatVersion(VersionNumber tomcatVersion) {
        if (tomcatVersion.compareTo(VersionNumber.version(10, 0)) >= 0) {
            return "2.1.0";
        }
        return "1.3.0";
    }
}
