package org.gradlex.jvm.dependency.conflict.test.patch

import org.gradlex.jvm.dependency.conflict.detection.rules.AlignmentDefinition
import org.gradlex.jvm.dependency.conflict.resolution.rules.LWJGLTargetPlatformRule
import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.IgnoreIf

class AddTargetPlatformVariantTest extends AbstractPatchTest {

    def "can add target variant"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.openjfx:javafx-base") {
                        addTargetPlatformVariant("", "none", "none")
                        addTargetPlatformVariant("mac", "macos", "x86-64")
                        addTargetPlatformVariant("mac-aarch64", "macos", "aarch64")
                        addTargetPlatformVariant("win", "windows", "x86-64")
                        addTargetPlatformVariant("linux-aarch64", "linux", "x86-64")
                    }
                }
            }
            configurations.compileClasspath {
                attributes.attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named("windows"))
                attributes.attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named("x86-64"))
            }
            dependencies {
                implementation("org.openjfx:javafx-base:17.0.10")
            }
            tasks.register("printJars") {
                println(configurations.compileClasspath.get().files.joinToString("\\n") { it.name });
            }
        """

        expect:
        String output = printJars().output
        output == 'javafx-base-17.0.10-win.jar\n'
    }

    @IgnoreIf({ GradleBuild.GRADLE6_TEST || GradleBuild.GRADLE7_TEST || GradleBuild.GRADLE8_0_TEST })
    def "adds lwjgl targets automatically - #moduleAndTarget"() {
        if (missingCombinations.contains(moduleAndTarget.toString())) return

        given:
        def module = moduleAndTarget[0]
        def target = moduleAndTarget[1]
        def artifact = module.split(':')[1]
        buildFile << """
            configurations.runtimeClasspath {
                attributes.attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named("${target.os}"))
                attributes.attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named("${target.arch}"))
            }
            dependencies {
                implementation("$module:3.3.6")
            }
            tasks.register("printJars") {
                println(configurations.runtimeClasspath.get().files.joinToString("\\n") { it.name });
            }
        """

        expect:
        dependenciesCompile()
        dependenciesRuntime()
        String output = printJars().output
        output.contains "$artifact-3.3.6.jar\n$artifact-3.3.6-${target.classifier}.jar\n"

        where:
        moduleAndTarget << (AlignmentDefinition.LWJGL.modules - [
                    "org.lwjgl:lwjgl-cuda",
                    "org.lwjgl:lwjgl-egl",
                    "org.lwjgl:lwjgl-fmod",
                    "org.lwjgl:lwjgl-jawt",
                    "org.lwjgl:lwjgl-odbc",
                    "org.lwjgl:lwjgl-opencl"]
        ).collectMany { m -> LWJGLTargetPlatformRule.TARGETS.collect { t -> [m, t] } }
    }

    def missingCombinations = [
            "[org.lwjgl:lwjgl-bgfx, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-ktx, natives-windows-x86|windows|x86]",
            "[org.lwjgl:lwjgl-meow, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-meow, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-meow, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-openvr, natives-freebsd|freebsd|x86-64]",
            "[org.lwjgl:lwjgl-openvr, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-openvr, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-openvr, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-openvr, natives-macos-arm64|macos|aarch64]",
            "[org.lwjgl:lwjgl-openvr, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-openxr, natives-macos|macos|x86-64]",
            "[org.lwjgl:lwjgl-openxr, natives-macos-arm64|macos|aarch64]",
            "[org.lwjgl:lwjgl-ovr, natives-freebsd|freebsd|x86-64]",
            "[org.lwjgl:lwjgl-ovr, natives-linux|linux|x86-64]",
            "[org.lwjgl:lwjgl-ovr, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-ovr, natives-linux-arm64|linux|aarch64]",
            "[org.lwjgl:lwjgl-ovr, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-ovr, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-ovr, natives-macos|macos|x86-64]",
            "[org.lwjgl:lwjgl-ovr, natives-macos-arm64|macos|aarch64]",
            "[org.lwjgl:lwjgl-ovr, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-remotery, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-sse, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-sse, natives-linux-arm64|linux|aarch64]",
            "[org.lwjgl:lwjgl-sse, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-sse, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-sse, natives-macos-arm64|macos|aarch64]",
            "[org.lwjgl:lwjgl-sse, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-tootle, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-tootle, natives-linux-arm64|linux|aarch64]",
            "[org.lwjgl:lwjgl-tootle, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-tootle, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-tootle, natives-macos-arm64|macos|aarch64]",
            "[org.lwjgl:lwjgl-tootle, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-vulkan, natives-freebsd|freebsd|x86-64]",
            "[org.lwjgl:lwjgl-vulkan, natives-linux|linux|x86-64]",
            "[org.lwjgl:lwjgl-vulkan, natives-linux-arm32|linux|arm]",
            "[org.lwjgl:lwjgl-vulkan, natives-linux-arm64|linux|aarch64]",
            "[org.lwjgl:lwjgl-vulkan, natives-linux-ppc64le|linux|ppc64le]",
            "[org.lwjgl:lwjgl-vulkan, natives-linux-riscv64|linux|riscv64]",
            "[org.lwjgl:lwjgl-vulkan, natives-windows|windows|x86-64]",
            "[org.lwjgl:lwjgl-vulkan, natives-windows-arm64|windows|aarch64]",
            "[org.lwjgl:lwjgl-vulkan, natives-windows-x86|windows|x86]"
    ]
}
