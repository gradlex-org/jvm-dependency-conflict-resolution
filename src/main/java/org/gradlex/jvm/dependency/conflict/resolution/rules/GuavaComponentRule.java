// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import java.util.Arrays;
import java.util.List;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradle.api.attributes.Attribute;

/**
 * Kept for individual usage to get the patch functionality for older Guava versions.
 * Might be removed in future versions.
 *
 * @deprecated use patch DSL:
 * <pre>
 *     jvmDependencyConflicts {
 *       patch {
 *         module("com.google.guava:guava") {
 *           reduceToCompileOnlyApiDependency("com.google.errorprone:error_prone_annotations")
 *           reduceToCompileOnlyApiDependency("com.google.j2objc:j2objc-annotations")
 *           reduceToCompileOnlyApiDependency("org.jspecify:jspecify")
 *         }
 *       }
 *     }
 * </pre>
 */
@CacheableRule
@Deprecated
public abstract class GuavaComponentRule implements ComponentMetadataRule {

    private static final Attribute<String> TARGET_JVM_ENVIRONMENT_ATTRIBUTE =
            Attribute.of("org.gradle.jvm.environment", String.class);

    private final List<String> RUNTIME_VARIANT_NAMES =
            Arrays.asList("runtime", "androidRuntimeElements", "jreRuntimeElements");

    public void execute(ComponentMetadataContext ctx) {
        int majorVersion = getMajorVersion(ctx.getDetails());
        // if (majorVersion <= 32) // May add this check should https://github.com/google/guava/pull/6606 be done
        removeAnnotationProcessorDependenciesFromRuntime(ctx.getDetails());

        if ((majorVersion >= 22 && majorVersion <= 31)
                || ctx.getDetails().getId().getVersion().startsWith("32.0")) {
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
        for (String runtime : RUNTIME_VARIANT_NAMES) {
            details.withVariant(
                    runtime,
                    variant -> variant.withDependencies(dependencies ->
                            dependencies.removeIf(dependency -> !guavaGroup.equals(dependency.getGroup()))));
        }
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

    private void addOtherJvmVariant(String baseVariantName, ComponentMetadataDetails details) {
        String version = getVersion(details);
        int majorVersion = getMajorVersion(details);
        boolean isAndroidVariantVersion = isAndroidVariantVersion(details);

        String env = isAndroidVariantVersion ? "android" : "standard-jvm";

        String otherJarSuffix =
                isAndroidVariantVersion ? "22.0".equals(version) || "23.0".equals(version) ? "" : "-jre" : "-android";
        String otherEnv = isAndroidVariantVersion ? "standard-jvm" : "android";
        String otherVariantName = isAndroidVariantVersion ? "standardJvm" : "android";

        details.withVariant(
                baseVariantName.toLowerCase(),
                variant -> variant.attributes(a -> {
                    a.attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, env);
                }));
        details.addVariant(otherVariantName + baseVariantName, baseVariantName.toLowerCase(), variant -> {
            variant.attributes(a -> {
                a.attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, otherEnv);
            });
            adjustDependenciesForGuava31AndLower(
                    variant, baseVariantName, version, majorVersion, isAndroidVariantVersion);
            variant.withFiles(files -> {
                files.removeAllFiles();
                files.addFile(
                        "guava-" + version + otherJarSuffix + ".jar",
                        "../" + version + otherJarSuffix + "/guava-" + version + otherJarSuffix + ".jar");
            });
        });
    }

    private void adjustDependenciesForGuava31AndLower(
            VariantMetadata variant,
            String baseVariantName,
            String version,
            int majorVersion,
            boolean isAndroidVariantVersion) {
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
    }

    private String checkerVersionFor(String guavaVersion, boolean androidVariant) {
        String name = androidVariant ? "checker-compat-qual" : "checker-qual";
        String version = "";
        if (androidVariant) {
            if (guavaVersion.equals("25.1")) {
                version = "2.0.0";
            } else if (guavaVersion.startsWith("28.")
                    || guavaVersion.startsWith("29.")
                    || guavaVersion.startsWith("30.")
                    || guavaVersion.startsWith("31.")) {
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
            } else if (guavaVersion.startsWith("26.") || guavaVersion.startsWith("27.")) {
                version = "2.5.2";
            } else if (guavaVersion.startsWith("25.")) {
                version = "2.0.0";
            }
        }

        return name + ":" + version;
    }
}
