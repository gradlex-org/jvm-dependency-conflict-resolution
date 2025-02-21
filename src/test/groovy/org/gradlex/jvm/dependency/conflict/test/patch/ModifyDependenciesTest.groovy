package org.gradlex.jvm.dependency.conflict.test.patch

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild

class ModifyDependenciesTest extends AbstractPatchTest {

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
}
