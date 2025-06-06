package org.gradlex.jvm.dependency.conflict.test.patch

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild

/**
 * All tests in this class come in two flavors:
 * - pom-only metadata variants: test against components that have been published with Maven, thus just having Maven pom
 *   metadata attached.
 * - Gradle metadata variants: always test against guava, because it is published with custom, non-standard Gradle
 *   metadata.
 */
class ModifyDependenciesTest extends AbstractPatchTest {

    def setup() {
        if (GradleBuild.GRADLE6_TEST) {
            buildFile << """
                configurations.configureEach {
                    attributes.attribute(Attribute.of("org.gradle.jvm.environment", String::class.java), "standard-jvm")
                }
            """
        }
    }

    def "can remove dependencies from pom-only metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("org.apache.commons:commons-text") {
                        removeDependency("org.apache.commons:commons-lang3")
                    }
                }
            }
            dependencies {
                implementation("org.apache.commons:commons-text:1.13.1")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- org.apache.commons:commons-text:1.13.1

'''

        and:
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- org.apache.commons:commons-text:1.13.1

'''
    }

    def "can remove dependencies from Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        removeDependency("com.google.errorprone:error_prone_annotations")
                        removeDependency("org.jspecify:jspecify")
                        removeDependency("com.google.j2objc:j2objc-annotations")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     \\--- com.google.guava:failureaccess:1.0.3
'''

        and:
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     \\--- com.google.guava:failureaccess:1.0.3
'''
    }

    def "can reduce dependency scope to runtime only for pom-only metadata variants"() {
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

    def "can reduce dependency scope to runtime only for Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        reduceToRuntimeOnlyDependency("com.google.errorprone:error_prone_annotations")
                        reduceToRuntimeOnlyDependency("org.jspecify:jspecify")
                        reduceToRuntimeOnlyDependency("com.google.j2objc:j2objc-annotations")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect: 'Dependencies are removed from compile classpath'
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     \\--- com.google.guava:failureaccess:1.0.3
'''

        and: 'Annotation libraries are present on the runtime classpath'
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     \\--- com.google.j2objc:j2objc-annotations:3.0.0
'''
    }

    def "can reduce dependency scope to compile only for pom-only metadata variants"() {
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

    def "can reduce dependency scope to compile only for Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                       reduceToCompileOnlyApiDependency("com.google.errorprone:error_prone_annotations")
                       reduceToCompileOnlyApiDependency("org.jspecify:jspecify")
                       reduceToCompileOnlyApiDependency("com.google.j2objc:j2objc-annotations")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect: 'All dependencies are present on the compile classpath'
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     \\--- com.google.j2objc:j2objc-annotations:3.0.0
'''

        and: 'Annotation libraries are not present on the runtime classpath'
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     \\--- com.google.guava:failureaccess:1.0.3
'''
    }

    def "can add api dependency for pom-only metadata variants"() {
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

    def "can add api dependency for Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        addApiDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     +--- com.google.j2objc:j2objc-annotations:3.0.0
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     +--- com.google.j2objc:j2objc-annotations:3.0.0
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
    }

    def "can add runtime only dependency for pom-only metadata variants"() {
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

    def "can add runtime only dependency for Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        addRuntimeOnlyDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     \\--- com.google.j2objc:j2objc-annotations:3.0.0

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     +--- com.google.j2objc:j2objc-annotations:3.0.0
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
    }

    def "can add compile only dependency for pom-only metadata variants"() {
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

    def "can add compile only dependency for Gradle metadata variants"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.google.guava:guava") {
                        addCompileOnlyApiDependency("io.projectreactor.tools:blockhound:1.0.8.RELEASE")
                    }
                }
            }
            dependencies {
                implementation("com.google.guava:guava:33.4.8-jre")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     +--- com.google.j2objc:j2objc-annotations:3.0.0
     \\--- io.projectreactor.tools:blockhound:1.0.8.RELEASE

'''
        dependenciesRuntime().output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
\\--- com.google.guava:guava:33.4.8-jre
     +--- com.google.guava:failureaccess:1.0.3
     +--- org.jspecify:jspecify:1.0.0
     +--- com.google.errorprone:error_prone_annotations:2.36.0
     \\--- com.google.j2objc:j2objc-annotations:3.0.0

'''
    }
}
