/*
 * Copyright 2022 the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.javaecosystem.capabilities.componentrules;

import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.attributes.Attribute;

@CacheableRule
abstract public class GuavaComponentRule implements ComponentMetadataRule {

    public static final String MODULE = "com.google.guava:guava";

    private final static Attribute<String> TARGET_JVM_ENVIRONMENT_ATTRIBUTE =
            Attribute.of("org.gradle.jvm.environment", String.class);

    public void execute(ComponentMetadataContext ctx) {
        int majorVersion = getMajorVersion(ctx.getDetails());
        int minorVersion = getMinorVersion(ctx.getDetails());
        if (majorVersion > 32 || (majorVersion == 20 && minorVersion > 1)) {
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

        String env = isAndroidVariantVersion ? "android" : "standard-jvm";

        String otherJarSuffix = isAndroidVariantVersion ?
                "22.0".equals(version) || "23.0".equals(version) ? "" : "-jre" : "-android";
        String otherEnv = isAndroidVariantVersion ? "standard-jvm" : "android";
        String otherVariantName = isAndroidVariantVersion ? "standardJvm" : "android";

        details.withVariant(baseVariantName.toLowerCase(), variant -> variant.attributes(a -> {
            a.attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, env);
        }));
        details.addVariant(otherVariantName + baseVariantName, baseVariantName.toLowerCase(), variant -> {
            variant.attributes(a -> {
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
            if(guavaVersion.startsWith("32.0")){
                version = "3.33.0";
            }
            else if (guavaVersion.equals("25.1")) {
                version = "2.0.0";
            } else if (guavaVersion.startsWith("28.") || guavaVersion.startsWith("29.") || guavaVersion.startsWith("30.") || guavaVersion.startsWith("31.")) {
                version = "2.5.5";
            } else {
                version = "2.5.2";
            }
        } else {
            if (guavaVersion.startsWith("32.")){
                version = "3.33.0";
            }
            else if (guavaVersion.startsWith("31.")) {
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