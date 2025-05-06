package org.gradlex.jvm.dependency.conflict.test.patch

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

    def "can add target variant with feature name"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.lwjgl:lwjgl") {
                        addTargetPlatformVariant("natives", "natives-linux", "linux", "x86-64")
                        addTargetPlatformVariant("natives", "natives-linux-arm64", "linux", "aarch64")
                        addTargetPlatformVariant("natives", "natives-macos", "macos", "x86-64")
                        addTargetPlatformVariant("natives", "natives-macos-arm64", "macos", "aarch64")
                        addTargetPlatformVariant("natives", "natives-windows", "windows", "x86-64")
                        addTargetPlatformVariant("natives", "natives-windows-arm64", "windows", "aarch64")
                    }
                }
            }
            configurations.runtimeClasspath {
                attributes.attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named("windows"))
                attributes.attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named("x86-64"))
            }
            dependencies {
                implementation("org.lwjgl:lwjgl:3.3.6")
                runtimeOnly("org.lwjgl:lwjgl:3.3.6") { capabilities { requireCapability("org.lwjgl:lwjgl-natives") } }
            }
            tasks.register("printJars") {
                println(configurations.runtimeClasspath.get().files.joinToString("\\n") { it.name });
            }
        """

        expect:
        dependenciesCompile()
        String output = printJars().output
        output == 'lwjgl-3.3.6.jar\nlwjgl-3.3.6-natives-windows.jar\n'
    }
}
