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
        """

        expect:
        String output = dependencyInsight("org.openjfx:javafx-base").output
        output.contains 'windows'
        output.contains 'x86-64'
    }
}
