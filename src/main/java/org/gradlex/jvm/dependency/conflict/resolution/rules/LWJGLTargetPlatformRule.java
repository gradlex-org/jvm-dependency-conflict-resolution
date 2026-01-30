// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import static org.gradle.nativeplatform.MachineArchitecture.*;
import static org.gradle.nativeplatform.OperatingSystemFamily.*;
import static org.gradlex.jvm.dependency.conflict.resolution.rules.DependencyModification.addCapability;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.MachineArchitecture;
import org.gradle.nativeplatform.OperatingSystemFamily;
import org.jspecify.annotations.NullMarked;

@NullMarked
@CacheableRule
public abstract class LWJGLTargetPlatformRule implements ComponentMetadataRule {

    public static class LWJGLTarget {
        private final String classifier;
        private final String os;
        private final String arch;

        LWJGLTarget(String classifier, String os, String arch) {
            this.classifier = classifier;
            this.os = os;
            this.arch = arch;
        }

        @Override
        public String toString() {
            return classifier + "|" + os + "|" + arch;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final List<LWJGLTarget> TARGETS = Arrays.asList(
            new LWJGLTarget("natives-freebsd", "freebsd", X86_64),
            new LWJGLTarget("natives-linux", LINUX, X86_64),
            new LWJGLTarget("natives-linux-arm32", LINUX, "arm"),
            new LWJGLTarget("natives-linux-arm64", LINUX, ARM64),
            new LWJGLTarget("natives-linux-ppc64le", LINUX, "ppc64le"),
            new LWJGLTarget("natives-linux-riscv64", LINUX, "riscv64"),
            new LWJGLTarget("natives-macos", MACOS, X86_64),
            new LWJGLTarget("natives-macos-arm64", MACOS, ARM64),
            new LWJGLTarget("natives-windows", WINDOWS, X86_64),
            new LWJGLTarget("natives-windows-arm64", WINDOWS, ARM64),
            new LWJGLTarget("natives-windows-x86", WINDOWS, X86));

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        String runtimeVariant = "runtime";
        String group = details.getId().getGroup();
        String name = details.getId().getName();
        String nativesCapability = name + "-natives";
        for (LWJGLTarget target : TARGETS) {
            addTargetPlatformVariant(details, target, runtimeVariant, nativesCapability);
        }
        details.withVariant(
                runtimeVariant,
                v -> v.withDependencies(dependencies ->
                        dependencies.add(group + ":" + name, d -> addCapability(d, group + ":" + nativesCapability))));
    }

    private void addTargetPlatformVariant(
            ComponentMetadataDetails details, LWJGLTarget target, String runtimeVariant, String nativesCapability) {
        String group = details.getId().getGroup();
        String name = details.getId().getName();
        String version = details.getId().getVersion();

        details.addVariant(target.classifier + "Runtime", runtimeVariant, variant -> {
            variant.withCapabilities(c -> {
                c.removeCapability(group, name);
                c.addCapability(group, nativesCapability, version);
            });
            variant.attributes(attributes -> {
                attributes.attribute(
                        OPERATING_SYSTEM_ATTRIBUTE, getObjects().named(OperatingSystemFamily.class, target.os));
                attributes.attribute(
                        ARCHITECTURE_ATTRIBUTE, getObjects().named(MachineArchitecture.class, target.arch));
            });
            variant.withFiles(files -> {
                files.removeAllFiles();
                files.addFile(name + "-" + version + "-" + target.classifier + ".jar");
            });
        });
    }
}
