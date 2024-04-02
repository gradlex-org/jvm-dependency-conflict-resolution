package org.gradlex.javaecosystem.capabilities.test

import org.gradlex.javaecosystem.capabilities.test.fixture.GradleBuild
import spock.lang.Specification

class CustomRulesTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        file('src/main/java/Dummy.java') << 'class Dummy {}'
        buildFile << """
            plugins {
                id("org.gradlex.jvm-ecosystem-conflict-resolution")
                id("java-library")
            }
            repositories.mavenCentral()
        """
    }

    def "can remove dependencies"() {
        given:
        if (GradleBuild.GRADLE6_TEST) {
            buildFile << """
                configurations.compileClasspath {
                    attributes.attribute(Attribute.of("org.gradle.jvm.environment", String::class.java), "standard-jvm")
                }
            """
        }
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        removeDependency("com.google.guava:listenablefuture")
                        removeDependency("com.google.code.findbugs:jsr305")
                        removeDependency("org.checkerframework:checker-qual")
                        removeDependency("com.google.errorprone:error_prone_annotations")
                        removeDependency("com.google.j2objc:j2objc-annotations")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.0.0-jre")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.0.0-jre
     \\--- com.google.guava:failureaccess:1.0.2
'''
    }

    def "can reduce dependency scope to runtime only"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.apache.commons:commons-text") {
                        reduceToRuntimeOnlyDependency("org.apache.commons:commons-lang3")
                    }
                }
            }
            dependencies {
                implementation("org.apache.commons:commons-text:1.11.0")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- org.apache.commons:commons-text:1.11.0

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- org.apache.commons:commons-text:1.11.0
     \\--- org.apache.commons:commons-lang3:3.13.0

'''
    }

    def "can reduce dependency scope to compile only"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.apache.commons:commons-text") {
                        reduceToCompileOnlyApiDependency("org.apache.commons:commons-lang3")
                    }
                }
            }
            dependencies {
                implementation("org.apache.commons:commons-text:1.11.0")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- org.apache.commons:commons-text:1.11.0
     \\--- org.apache.commons:commons-lang3:3.13.0

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- org.apache.commons:commons-text:1.11.0

'''
    }

    def "can add api dependency"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("io.netty:netty-common") {
                        addApiDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("io.netty:netty-common:4.1.106.Final")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
    }

    def "can add runtime only dependency"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("io.netty:netty-common") {
                        addRuntimeOnlyDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("io.netty:netty-common:4.1.106.Final")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
    }

    def "can add compile only dependency"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("io.netty:netty-common") {
                        addCompileOnlyApiDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("io.netty:netty-common:4.1.106.Final")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final

'''
    }

    def "can add capability by enum"() {
        given:
        buildFile.text = 'import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions.STAX_API\n' + buildFile.text
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
        fail().output.contains "Cannot select module with conflict on capability 'commons-lang:commons-lang:2.6' also provided by [org.apache.commons:commons-lang3:3.11(compile)]"
    }

    def "can remove capability by enum"() {
        given:
        buildFile.text = 'import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions.STAX_API\n' + buildFile.text
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

    def "can add feature"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("io.netty:netty-transport-native-epoll") {
                        addFeature("linux-x86_64")
                        addFeature("linux-aarch_64")
                    }
                }
            }
            dependencies {
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final")
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final") {
                    capabilities { requireCapabilities("io.netty:netty-transport-native-epoll-linux-x86_64") }
                }
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final") {
                    capabilities { requireCapabilities("io.netty:netty-transport-native-epoll-linux-aarch_64") }
                }
            }
        """

        expect:
        def output = dependencyInsight('io.netty:netty-transport-native-epoll').output
        output.contains 'linux-x86_64Compile'
        output.contains 'linux-aarch_64Compile'
    }

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

    def "can add alignment"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    align(
                        "org.apache.poi:poi",
                        "org.apache.poi:poi-excelant",
                        "org.apache.poi:poi-ooxml",
                        "org.apache.poi:poi-scratchpad"
                    )
                }
            }
            dependencies {
                implementation("org.apache.poi:poi:5.2.5")
                implementation("org.apache.poi:poi-excelant")
                implementation("org.apache.poi:poi-ooxml")
                implementation("org.apache.poi:poi-scratchpad")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- org.apache.poi:poi:5.2.5
|    +--- commons-codec:commons-codec:1.16.0
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- org.apache.commons:commons-math3:3.6.1
|    +--- commons-io:commons-io:2.15.0
|    +--- com.zaxxer:SparseBitSet:1.3
|    +--- org.apache.logging.log4j:log4j-api:2.21.1
|    |    \\--- org.apache.logging.log4j:log4j-bom:2.21.1
|    |         \\--- org.apache.logging.log4j:log4j-api:2.21.1 (c)
|    +--- org.apache.poi:poi:5.2.5 (c)
|    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
+--- org.apache.poi:poi-excelant -> 5.2.5
|    +--- org.apache.ant:ant:1.10.14
|    |    \\--- org.apache.ant:ant-launcher:1.10.14
|    +--- org.apache.poi:poi-ooxml:5.2.5
|    |    +--- org.apache.poi:poi:5.2.5 (*)
|    |    +--- org.apache.poi:poi-ooxml-lite:5.2.5
|    |    |    \\--- org.apache.xmlbeans:xmlbeans:5.2.0
|    |    |         \\--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
|    |    +--- org.apache.xmlbeans:xmlbeans:5.2.0 (*)
|    |    +--- org.apache.commons:commons-compress:1.25.0
|    |    +--- commons-io:commons-io:2.15.0
|    |    +--- com.github.virtuald:curvesapi:1.08
|    |    +--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
|    |    +--- org.apache.commons:commons-collections4:4.4
|    |    +--- org.apache.poi:poi:5.2.5 (c)
|    |    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    |    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    |    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
|    +--- org.apache.poi:poi:5.2.5 (c)
|    +--- org.apache.poi:poi-excelant:5.2.5 (c)
|    +--- org.apache.poi:poi-ooxml:5.2.5 (c)
|    \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
+--- org.apache.poi:poi-ooxml -> 5.2.5 (*)
\\--- org.apache.poi:poi-scratchpad -> 5.2.5
     +--- org.apache.poi:poi:5.2.5 (*)
     +--- org.apache.logging.log4j:log4j-api:2.21.1 (*)
     +--- org.apache.commons:commons-math3:3.6.1
     +--- commons-codec:commons-codec:1.16.0
     +--- org.apache.poi:poi:5.2.5 (c)
     +--- org.apache.poi:poi-excelant:5.2.5 (c)
     +--- org.apache.poi:poi-ooxml:5.2.5 (c)
     \\--- org.apache.poi:poi-scratchpad:5.2.5 (c)
'''
    }

    def "can add alignment via BOM"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    alignWithBom("org.ow2.asm:asm-bom",
                        "org.ow2.asm:asm",
                        "org.ow2.asm:asm-tree",
                        "org.ow2.asm:asm-analysis",
                        "org.ow2.asm:asm-util",
                        "org.ow2.asm:asm-commons"
                    )
                }
            }
            dependencies {
                implementation("org.ow2.asm:asm")
                implementation("org.ow2.asm:asm-tree:9.6")
                implementation("org.ow2.asm:asm-analysis")
                implementation("org.ow2.asm:asm-util")
                implementation("org.ow2.asm:asm-commons")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- org.ow2.asm:asm -> 9.6
|    \\--- org.ow2.asm:asm-bom:9.6
|         +--- org.ow2.asm:asm:9.6 (c)
|         +--- org.ow2.asm:asm-tree:9.6 (c)
|         +--- org.ow2.asm:asm-analysis:9.6 (c)
|         +--- org.ow2.asm:asm-util:9.6 (c)
|         \\--- org.ow2.asm:asm-commons:9.6 (c)
+--- org.ow2.asm:asm-tree:9.6
|    +--- org.ow2.asm:asm:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
+--- org.ow2.asm:asm-analysis -> 9.6
|    +--- org.ow2.asm:asm-tree:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
+--- org.ow2.asm:asm-util -> 9.6
|    +--- org.ow2.asm:asm:9.6 (*)
|    +--- org.ow2.asm:asm-tree:9.6 (*)
|    +--- org.ow2.asm:asm-analysis:9.6 (*)
|    \\--- org.ow2.asm:asm-bom:9.6 (*)
\\--- org.ow2.asm:asm-commons -> 9.6
     +--- org.ow2.asm:asm:9.6 (*)
     +--- org.ow2.asm:asm-tree:9.6 (*)
     \\--- org.ow2.asm:asm-bom:9.6 (*)
'''
    }

    def "can set component status to integration for certain versions"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.fasterxml.jackson.core:jackson-core") {
                        setStatusToIntegration("-m", "-rc")
                    }
                }
            }
            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-core:2.15.0-rc3")
            }
        """

        expect:
        dependencyInsight("com.fasterxml.jackson.core:jackson-core").output.contains 'integration'
    }
}
