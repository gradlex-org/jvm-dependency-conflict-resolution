package de.jjohannes.gradle.javaecosystem.capabilities.componentrules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.attributes.Attribute;

import static org.gradle.api.attributes.java.TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE;

@CacheableRule
abstract public class GuavaComponentRule implements ComponentMetadataRule {

    public static final String MODULE = "com.google.guava:guava";

    private final static Attribute<String> TARGET_JVM_ENVIRONMENT_ATTRIBUTE =
            Attribute.of("org.gradle.jvm.environment", String.class);

    public void execute(ComponentMetadataContext ctx) {
        int majorVersion = getMajorVersion(ctx.getDetails());
        int minorVersion = getMinorVersion(ctx.getDetails());
        if (majorVersion > 31 || (majorVersion == 20 && minorVersion > 1)) {
            return;
        }

        removeAnnotationProcessorDependenciesFromRuntime(ctx.getDetails());

        if (majorVersion >= 22) {
            removeAnimalSnifferAnnotations(ctx.getDetails());

            addOtherJvmVariant("Compile", ctx.getDetails());
            addOtherJvmVariant("Runtime", ctx.getDetails());
        }
    }

    private void removeAnimalSnifferAnnotations(ComponentMetadataDetails details) {
        // this is not needed - https://github.com/google/guava/issues/2824
        details.allVariants(variant -> variant.withDependencies(dependencies ->
                dependencies.removeIf(dependency -> "animal-sniffer-annotations".equals(dependency.getName()))));
    }

    private void removeAnnotationProcessorDependenciesFromRuntime(ComponentMetadataDetails details) {
        // everything outside the 'com.google.guava' group is an annotation processor
        String guavaGroup = details.getId().getGroup();
        details.withVariant("runtime", variant -> variant.withDependencies(dependencies ->
                dependencies.removeIf(dependency -> !guavaGroup.equals(dependency.getGroup()))));
    }

    private boolean isAndroidVariantVersion(ComponentMetadataDetails details) {
        return details.getId().getVersion().endsWith("-android");
    }

    private String getVersion(ComponentMetadataDetails details) {
        String versionString = details.getId().getVersion();
        if (!versionString.contains("-")) {
            return versionString;
        }
        return versionString.substring(0, versionString.indexOf("-"));
    }

    private int getMajorVersion(ComponentMetadataDetails details) {
        String version = getVersion(details);
        return Integer.parseInt(version.substring(0, version.indexOf(".")));
    }

    private int getMinorVersion(ComponentMetadataDetails details) {
        String version = getVersion(details);
        int minorIndex = version.indexOf(".") + 1;
        return Integer.parseInt(version.substring(minorIndex, minorIndex + 1));
    }

    private void addOtherJvmVariant(String baseVariantName, ComponentMetadataDetails details) {
        String version = getVersion(details);
        int majorVersion = getMajorVersion(details);
        boolean isAndroidVariantVersion = isAndroidVariantVersion(details);

        int jdkVersion = isAndroidVariantVersion ? majorVersion < 31 ? 6 : 8 : 8;
        String env = isAndroidVariantVersion ? "android" : "standard-jvm";

        String otherJarSuffix = isAndroidVariantVersion ?
                "22.0".equals(version) || "23.0".equals(version) ? "" : "-jre" : "-android";
        int otherJdkVersion = isAndroidVariantVersion ? 8 : majorVersion < 31 ? 6 : 8;
        String otherEnv = isAndroidVariantVersion ? "standard-jvm" : "android";
        String otherVariantName = isAndroidVariantVersion ? "standardJvm" : "android";

        details.withVariant(baseVariantName.toLowerCase(), variant -> variant.attributes(a -> {
            a.attribute(TARGET_JVM_VERSION_ATTRIBUTE, jdkVersion);
            a.attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, env);
        }));
        details.addVariant(otherVariantName + baseVariantName, baseVariantName.toLowerCase(), variant -> {
            variant.attributes(a -> {
                a.attribute(TARGET_JVM_VERSION_ATTRIBUTE, otherJdkVersion);
                a.attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, otherEnv);
            });
            if ((majorVersion >= 26 && majorVersion < 31) || version.startsWith("31.0") || "25.1".equals(version)) {
                variant.withDependencies(dependencies -> {
                    if (majorVersion < 31 || isAndroidVariantVersion) {
                        dependencies.removeIf(d -> "org.checkerframework".equals(d.getGroup()));
                    }
                    if (baseVariantName.equals("Compile")) {
                        dependencies.add("org.checkerframework:" + checkerVersionFor(version, !isAndroidVariantVersion));
                    }
                });
            }
            variant.withFiles(files -> {
                files.removeAllFiles();
                files.addFile("guava-" + version + otherJarSuffix + ".jar",
                        "../" + version + otherJarSuffix + "/guava-" + version + otherJarSuffix + ".jar");
            });
        });
    }

    private String checkerVersionFor(String guavaVersion, boolean androidVariant) {
        String name = androidVariant ? "checker-compat-qual" : "checker-qual";
        String version = "";
        if (androidVariant) {
            if (guavaVersion.equals("25.1")) {
                version = "2.0.0";
            } else if (guavaVersion.startsWith("28.") || guavaVersion.startsWith("29.") || guavaVersion.startsWith("30.") || guavaVersion.startsWith("31.")) {
                version = "2.5.5";
            } else {
                version = "2.5.2";
            }
        } else {
            if (guavaVersion.startsWith("31.")) {
                version = "3.12.0";
            } else if (guavaVersion.equals("30.1.1")) {
                version = "3.8.0";
            } else if (guavaVersion.startsWith("30.")) {
                version = "3.5.0";
            } else if (guavaVersion.startsWith("29.")) {
                version = "2.11.1";
            } else if (guavaVersion.equals("28.2")) {
                version = "2.10.0";
            } else if (guavaVersion.startsWith("28.")) {
                version = "2.8.1";
            } else  if (guavaVersion.startsWith("26.") || guavaVersion.startsWith("27.")) {
                version = "2.5.2";
            } else if (guavaVersion.startsWith("25.")) {
                version = "2.0.0";
            }
        }

        return name + ":" + version;
    }
}