package org.gradlex.jvm.dependency.conflict.test.patch

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild

class ModifyCapabilitiesTest extends AbstractPatchTest {

    def "can add capability by enum"() {
        given:
        buildFile.text = 'import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition.STAX_API\n' + buildFile.text
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.eclipse.birt.runtime:javax.xml.stream") {
                        addCapability(STAX_API)
                    }
                }
            }
            dependencies {
                implementation("stax:stax-api:1.0.1")
                implementation("org.eclipse.birt.runtime:javax.xml.stream:1.0.1.v201004272200")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- stax:stax-api:1.0.1
\\--- org.eclipse.birt.runtime:javax.xml.stream:1.0.1.v201004272200 -> stax:stax-api:1.0.1

'''
    }

    def "can add capability by string"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.apache.commons:commons-lang3") {
                        addCapability("commons-lang:commons-lang") // artificial case for testing!
                    }
                }
            }
            dependencies {
                implementation("commons-lang:commons-lang:2.6")
                implementation("org.apache.commons:commons-lang3:3.11")
            }
        """

        expect:
        GradleBuild.GRADLE9_1_TEST
                ? fail().output.contains("Cannot select module with conflict on capability 'commons-lang:commons-lang:3.11' also provided by ['commons-lang:commons-lang:2.6' (compile)]")
                : fail().output.contains("Cannot select module with conflict on capability 'commons-lang:commons-lang:2.6' also provided by [org.apache.commons:commons-lang3:3.11(compile)]")
    }

    def "can remove capability by enum"() {
        given:
        buildFile.text = 'import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition.STAX_API\n' + buildFile.text
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("javax.xml.stream:stax-api") {
                        removeCapability(STAX_API)
                    }
                }
            }
            dependencies {
                implementation("stax:stax-api:1.0.1")
                implementation("javax.xml.stream:stax-api:1.0-2")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- stax:stax-api:1.0.1
\\--- javax.xml.stream:stax-api:1.0-2

'''
    }

    def "can remove capability by string"() {
        given:
        if (GradleBuild.GRADLE6_TEST) {
            buildFile << """
                configurations.runtimeClasspath {
                    attributes.attribute(Attribute.of("org.gradle.jvm.environment", String::class.java), "standard-jvm")
                }
            """
        }

        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        removeCapability("com.google.collections:google-collections")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.1.0-jre")
                implementation("com.google.collections:google-collections:1.0")
            }
        """

        expect:
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
+--- com.google.guava:guava:33.1.0-jre
|    +--- com.google.guava:failureaccess:1.0.2
|    +--- com.google.code.findbugs:jsr305:3.0.2
|    +--- org.checkerframework:checker-qual:3.42.0
|    \\--- com.google.errorprone:error_prone_annotations:2.26.1
\\--- com.google.collections:google-collections:1.0

'''
    }
}
