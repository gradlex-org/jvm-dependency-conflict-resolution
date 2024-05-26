package org.gradlex.jvm.dependency.conflict.test

import org.gradle.testkit.runner.BuildResult
import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Specification

class ConsistentResolutionTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def subprojects = ['app', 'service', 'component']

    def setup() {
        settingsFile << """
            dependencyResolutionManagement { repositories.mavenCentral() }
            include(${subprojects.collect { "\":$it\"" }.join(',')})
        """
        subprojects.each {
            file("$it/build.gradle.kts") << '''
                plugins {
                    id("java-library")
                    id("org.gradlex.jvm-dependency-conflict-resolution")
                }
                dependencies { implementation(project(":component")) }
            '''
        }
    }

    def "consistently resolves with version providing projects"() {
        given:
        subprojects.each {
            file("$it/build.gradle.kts") << '''
            jvmDependencyConflicts.consistentResolution {
                providesVersions(":app")
                providesVersions(":service")
            }
        '''
        }
        file('app/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-lang3:3.11")
            }
        '''
        file('service/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-math3:3.6")
            }
        '''
        file('component/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-lang3:3.9")
                implementation("org.apache.commons:commons-math3:3.5")
            }
        '''

        expect:
        dependenciesComponent('runtimeClasspath').output.contains '''
runtimeClasspath - Runtime classpath of source set 'main'.
+--- project :component (*)
+--- org.apache.commons:commons-lang3:3.9 -> 3.11
+--- org.apache.commons:commons-math3:3.5 -> 3.6
+--- org.apache.commons:commons-lang3:{strictly 3.11} -> 3.11 (c)
\\--- org.apache.commons:commons-math3:{strictly 3.6} -> 3.6 (c)
'''
    }

    def "consistently resolves with version providing projects and platform"() {
        settingsFile << 'include(":versions")'
        file("versions/build.gradle.kts") << '''
            plugins { id("java-platform") }
            dependencies.constraints {
                api("org.apache.commons:commons-lang3:3.9")
                api("org.apache.commons:commons-text:1.9")
                api("org.junit.jupiter:junit-jupiter-api:5.9.2")
            }
        '''
        subprojects.each {
            file("$it/build.gradle.kts") << '''
            jvmDependencyConflicts.consistentResolution {
                platform(":versions")
                providesVersions(":app")
                providesVersions(":service")
            }
        '''
        }
        file('app/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-lang3")
            }
        '''
        file('service/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-text")
            }
        '''
        file('component/build.gradle.kts') << '''
            dependencies {
                implementation("org.apache.commons:commons-lang3")
                testImplementation("org.junit.jupiter:junit-jupiter-params")
                testRuntimeOnly("org.junit.jupiter:junit-jupiter-api")
            }
        '''

        expect:
        dependenciesComponent('testCompileClasspath').output.contains '''
testCompileClasspath - Compile classpath for source set 'test'.
+--- project :component (*)
+--- org.apache.commons:commons-lang3 -> 3.11
+--- project :versions
|    +--- org.apache.commons:commons-lang3:3.9 -> 3.11 (c)
|    \\--- org.junit.jupiter:junit-jupiter-api:5.9.2 (c)
+--- org.junit.jupiter:junit-jupiter-params -> 5.9.2
|    +--- org.junit:junit-bom:5.9.2
|    |    +--- org.junit.jupiter:junit-jupiter-api:5.9.2 (c)
|    |    +--- org.junit.jupiter:junit-jupiter-params:5.9.2 (c)
|    |    \\--- org.junit.platform:junit-platform-commons:1.9.2 (c)
|    +--- org.junit.jupiter:junit-jupiter-api:5.9.2
|    |    +--- org.junit:junit-bom:5.9.2 (*)
|    |    +--- org.opentest4j:opentest4j:1.2.0
|    |    +--- org.junit.platform:junit-platform-commons:1.9.2
|    |    |    +--- org.junit:junit-bom:5.9.2 (*)
|    |    |    \\--- org.apiguardian:apiguardian-api:1.1.2
|    |    \\--- org.apiguardian:apiguardian-api:1.1.2
|    \\--- org.apiguardian:apiguardian-api:1.1.2
+--- org.apache.commons:commons-lang3:{strictly 3.11} -> 3.11 (c)
+--- org.junit.jupiter:junit-jupiter-params:{strictly 5.9.2} -> 5.9.2 (c)
+--- org.junit:junit-bom:{strictly 5.9.2} -> 5.9.2 (c)
+--- org.junit.jupiter:junit-jupiter-api:{strictly 5.9.2} -> 5.9.2 (c)
+--- org.opentest4j:opentest4j:{strictly 1.2.0} -> 1.2.0 (c)
\\--- org.junit.platform:junit-platform-commons:{strictly 1.9.2} -> 1.9.2 (c)
'''

        dependenciesComponent('testRuntimeClasspath').output.contains '''
testRuntimeClasspath - Runtime classpath of source set 'test'.
+--- project :component (*)
+--- org.apache.commons:commons-lang3 -> 3.11
+--- project :versions
|    +--- org.apache.commons:commons-lang3:3.9 -> 3.11 (c)
|    \\--- org.junit.jupiter:junit-jupiter-api:5.9.2 (c)
+--- org.junit.jupiter:junit-jupiter-params -> 5.9.2
|    +--- org.junit:junit-bom:5.9.2
|    |    +--- org.junit.jupiter:junit-jupiter-api:5.9.2 (c)
|    |    +--- org.junit.jupiter:junit-jupiter-params:5.9.2 (c)
|    |    \\--- org.junit.platform:junit-platform-commons:1.9.2 (c)
|    \\--- org.junit.jupiter:junit-jupiter-api:5.9.2
|         +--- org.junit:junit-bom:5.9.2 (*)
|         +--- org.opentest4j:opentest4j:1.2.0
|         \\--- org.junit.platform:junit-platform-commons:1.9.2
|              \\--- org.junit:junit-bom:5.9.2 (*)
+--- org.junit.jupiter:junit-jupiter-api -> 5.9.2 (*)
\\--- org.apache.commons:commons-lang3:{strictly 3.11} -> 3.11 (c)
'''
        dependenciesApp('testRuntimeClasspath').output.contains '''
testRuntimeClasspath - Runtime classpath of source set 'test'.
+--- project :component
|    +--- project :component (*)
|    \\--- org.apache.commons:commons-lang3 -> 3.11
+--- org.apache.commons:commons-lang3 -> 3.11
+--- project :versions
|    \\--- org.apache.commons:commons-lang3:3.9 -> 3.11 (c)
\\--- org.apache.commons:commons-lang3:{strictly 3.11} -> 3.11 (c)
'''
    }

    private BuildResult dependenciesApp(String configuration) {
        runner(':app:dependencies', "--configuration=$configuration").build()
    }

    private BuildResult dependenciesComponent(String configuration) {
        runner(':component:dependencies', "--configuration=$configuration").build()
    }

}
