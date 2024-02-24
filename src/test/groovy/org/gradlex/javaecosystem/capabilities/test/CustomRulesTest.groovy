package org.gradlex.javaecosystem.capabilities.test

import org.gradlex.javaecosystem.capabilities.test.fixture.GradleBuild
import spock.lang.IgnoreIf
import spock.lang.Specification

@IgnoreIf({ GradleBuild.GRADLE6_TEST || GradleBuild.GRADLE7_TEST })
class CustomRulesTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        file('src/main/java/Dummy.java') << 'class Dummy {}'
        buildFile << """
            import org.gradlex.javaecosystem.capabilities.customrules.*
            plugins {
                id("org.gradlex.java-ecosystem-capabilities")
                id("java-library")
            }
            repositories.mavenCentral()
        """
    }

    def "can remove dependencies"() {
        given:
        buildFile << """
            dependencies.components {
                withModule<RemoveDependenciesMetadataRule>("com.google.guava:guava") { params(listOf(
                    "com.google.guava:listenablefuture",
                    "com.google.code.findbugs:jsr305",
                    "org.checkerframework:checker-qual",
                    "com.google.errorprone:error_prone_annotations",
                    "com.google.j2objc:j2objc-annotations"
                ))}
            }
            dependencies {
                implementation("com.google.guava:guava:33.0.0-jre")
            }
        """

        expect:
        dependencies().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.0.0-jre
     \\--- com.google.guava:failureaccess:1.0.2
'''
    }

    def "can add dependency"() {
        given:
        buildFile << """
            dependencies.components {
                withModule<AddDependenciesMetadataRule>("io.netty:netty-common") { params(listOf(
                    "io.projectreactor.tools:blockhound:1.0.8.RELEASE"
                ))}
   
            }
            dependencies {
                implementation("io.netty:netty-common:4.1.106.Final")
            }
        """

        expect:
        dependencies().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- io.netty:netty-common:4.1.106.Final
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE
'''
    }

    def "can add capability"() {
        given:
        buildFile << """
            dependencies.components {
                withModule<AddCapabilityMetadataRule>("org.apache.commons:commons-lang3") { params(
                    "commons-lang:commons-lang" // artificial case for testing!
                )}
   
            }
            dependencies {
                implementation("commons-lang:commons-lang:2.6")
                implementation("org.apache.commons:commons-lang3:3.11")
            }
        """

        expect:
        fail().output.contains "Cannot select module with conflict on capability 'commons-lang:commons-lang:2.6' also provided by [org.apache.commons:commons-lang3:3.11(compile)]"
    }

    def "can add feature"() {
        given:
        buildFile << """
            dependencies.components {
                withModule<AddFeaturesMetadataRule>("io.netty:netty-transport-native-epoll") { params(listOf(
                    "linux-x86_64", "linux-aarch_64"
                ))}
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
        output.contains '''Variant compile:
    | Attribute Name                 | Provided | Requested    |
    |--------------------------------|----------|--------------|
    | org.gradle.status              | release  |              |
    | org.gradle.category            | library  | library      |
    | org.gradle.libraryelements     | jar      | classes      |
    | org.gradle.usage               | java-api | java-api     |'''
        output.contains '''Variant linux-x86_64Compile:
    | Attribute Name                 | Provided | Requested    |
    |--------------------------------|----------|--------------|
    | org.gradle.status              | release  |              |
    | org.gradle.category            | library  | library      |
    | org.gradle.libraryelements     | jar      | classes      |
    | org.gradle.usage               | java-api | java-api     |'''
        output.contains '''Variant linux-aarch_64Compile:
    | Attribute Name                 | Provided | Requested    |
    |--------------------------------|----------|--------------|
    | org.gradle.status              | release  |              |
    | org.gradle.category            | library  | library      |
    | org.gradle.libraryelements     | jar      | classes      |
    | org.gradle.usage               | java-api | java-api     |'''
    }

    def "can add target variant"() {
        given:
        buildFile << """
            dependencies.components {
                withModule<AddTargetPlatformVariantsMetadataRule>("org.openjfx:javafx-base") { params(listOf(
                    Target("", "none", "none"),
                    Target("mac", "macos", "x86-64"),
                    Target("mac-aarch64", "macos", "aarch64"),
                    Target("win", "windows", "x86-64"),
                    Target("linux-aarch64", "linux", "x86-64")
                )) }
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
        dependencyInsight("org.openjfx:javafx-base").output.contains '''org.openjfx:javafx-base:17.0.10
  Variant winCompile:
    | Attribute Name                    | Provided | Requested    |
    |-----------------------------------|----------|--------------|
    | org.gradle.status                 | release  |              |
    | org.gradle.category               | library  | library      |
    | org.gradle.libraryelements        | jar      | classes      |
    | org.gradle.native.architecture    | x86-64   | x86-64       |
    | org.gradle.native.operatingSystem | windows  | windows      |
    | org.gradle.usage                  | java-api | java-api     |
'''
    }

    def "can add alignment"() {
        given:
        buildFile << """
            dependencies.components {
                val poiComponents = listOf(
                    "org.apache.poi:poi",
                    "org.apache.poi:poi-excelant",
                    "org.apache.poi:poi-ooxml",
                    "org.apache.poi:poi-scratchpad"
                )
                poiComponents.forEach {
                    withModule<AddAlignmentConstraintsMetadataRule>(it) { params(poiComponents) }
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
        dependencies().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- org.apache.poi:poi:5.2.5
|    +--- commons-codec:commons-codec:1.16.0
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- org.apache.commons:commons-math3:3.6.1
|    +--- commons-io:commons-io:2.15.0
|    +--- com.zaxxer:SparseBitSet:1.3
|    +--- org.apache.logging.log4j:log4j-api:2.21.1
|    |    \\--- org.apache.logging.log4j:log4j-bom:2.21.1
|    |         +--- org.apache.logging.log4j:log4j-bom:2.21.1 (*)
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
            dependencies.components {
                val asmComponents = listOf(
                    "org.ow2.asm:asm",
                    "org.ow2.asm:asm-tree",
                    "org.ow2.asm:asm-analysis",
                    "org.ow2.asm:asm-util",
                    "org.ow2.asm:asm-commons"
                )
                asmComponents.forEach {
                    withModule<AddBomDependencyMetadataRule>(it) { params("org.ow2.asm:asm-bom") }
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
        dependencies().output.contains '''
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
}
