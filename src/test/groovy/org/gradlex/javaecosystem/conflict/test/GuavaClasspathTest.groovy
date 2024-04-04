package org.gradlex.javaecosystem.conflict.test

import org.gradlex.javaecosystem.conflict.test.fixture.GradleBuild
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradlex.javaecosystem.conflict.test.fixture.GradleBuild.GRADLE6_TEST

class GuavaClasspathTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        settingsFile << 'rootProject.name = "test-project"'
    }

    static allGuavaVersions() {
        [
                ['32.1.1', 'jre'    , [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['32.1.1', 'android', [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['32.0.1', 'jre'    , [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['32.0.1', 'android', [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['32.0.0', 'jre'    , [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['32.0.0', 'android', [errorProne:  '2.18.0', j2objc: '2.8', jsr305: '3.0.2', checker: '3.33.0', failureaccess: '1.0.1']],
                ['31.1'  , 'jre'    , [errorProne:  '2.11.0', j2objc: '1.3', jsr305: '3.0.2', checker: '3.12.0', failureaccess: '1.0.1']],
                ['31.1'  , 'android', [errorProne:  '2.11.0', j2objc: '1.3', jsr305: '3.0.2', checker: '3.12.0', failureaccess: '1.0.1']],
                ['31.0.1', 'jre'    , [errorProne:  '2.7.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.12.0', failureaccess: '1.0.1']],
                ['31.0.1', 'android', [errorProne:  '2.7.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.12.0', failureaccess: '1.0.1']],
                ['31.0'  , 'jre'    , [errorProne:  '2.7.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.12.0', failureaccess: '1.0.1']],
                ['31.0'  , 'android', [errorProne:  '2.7.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.12.0', failureaccess: '1.0.1']],
                ['30.1.1', 'jre'    , [errorProne:  '2.5.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.8.0', failureaccess: '1.0.1']],
                ['30.1.1', 'android', [errorProne:  '2.5.1', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.8.0', failureaccess: '1.0.1']],
                ['30.1'  , 'jre'    , [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.5.0', failureaccess: '1.0.1']],
                ['30.1'  , 'android', [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.5.0', failureaccess: '1.0.1']],
                ['30.0'  , 'jre'    , [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.5.0', failureaccess: '1.0.1']],
                ['30.0'  , 'android', [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '3.5.0', failureaccess: '1.0.1']],
                ['29.0'  , 'jre'    , [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '2.11.1', failureaccess: '1.0.1']],
                ['29.0'  , 'android', [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '2.11.1', failureaccess: '1.0.1']],
                ['28.2'  , 'jre'    , [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '2.10.0', failureaccess: '1.0.1']],
                ['28.2'  , 'android', [errorProne:  '2.3.4', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker: '2.10.0', failureaccess: '1.0.1']],
                ['28.1'  , 'jre'    , [errorProne:  '2.3.2', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker:  '2.8.1', failureaccess: '1.0.1']],
                ['28.1'  , 'android', [errorProne:  '2.3.2', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker:  '2.8.1', failureaccess: '1.0.1']],
                ['28.0'  , 'jre'    , [errorProne:  '2.3.2', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker:  '2.8.1', failureaccess: '1.0.1']],
                ['28.0'  , 'android', [errorProne:  '2.3.2', j2objc: '1.3', jsr305: '3.0.2', checkerCompat:  '2.5.5', checker:  '2.8.1', failureaccess: '1.0.1']],
                ['27.1'  , 'jre'    , [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0.1']],
                ['27.1'  , 'android', [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0.1']],
                ['27.0.1', 'jre'    , [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0.1']],
                ['27.0.1', 'android', [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0.1']],
                ['27.0'  , 'jre'    , [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0']],
                ['27.0'  , 'android', [errorProne:  '2.2.0', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2', failureaccess: '1.0']],
                ['26.0'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2']],
                ['26.0'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.5.2', checker:  '2.5.2']],
                ['25.1'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.0.0', checker:  '2.0.0']],
                ['25.1'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '3.0.2', checkerCompat:  '2.0.0', checker:  '2.0.0']],
                ['25.0'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['25.0'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.1.1', 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.1.1', 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.1'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.1'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.0'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['24.0'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['23.6.1', 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['23.6.1', 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['23.6'  , 'jre'    , [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['23.6'  , 'android', [errorProne:  '2.1.3', j2objc: '1.1', jsr305: '1.3.9', checkerCompat:  '2.0.0']],
                ['23.5'  , 'jre'    , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9', checker:  '2.0.0']],
                ['23.5'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9', checker:  '2.0.0']],
                ['23.4'  , 'jre'    , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.4'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.3'  , 'jre'    , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.3'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.2'  , 'jre'    , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.2'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.1'  , 'jre'    , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.1'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.0'  , ''       , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['23.0'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['22.0'  , ''       , [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['22.0'  , 'android', [errorProne: '2.0.18', j2objc: '1.1', jsr305: '1.3.9']],
                ['21.0'  , ''       , [:]],
                ['20.0'  , ''       , [:]],
                ['19.0'  , ''       , [:]],
                ['18.0'  , ''       , [:]],
                ['17.0'  , ''       , [:]],
                ['16.0.1', ''       , [:]],
                ['16.0'  , ''       , [:]],
                ['15.0'  , ''       , [:]],
                ['14.0.1', ''       , [:]],
                ['14.0'  , ''       , [:]],
                ['13.0.1', ''       , [:]],
                ['12.0.1', ''       , [jsr305: '1.3.9']],
                ['12.0'  , ''       , [jsr305: '1.3.9']],
                ['11.0.2', ''       , [jsr305: '1.3.9']],
                ['11.0.1', ''       , [jsr305: '1.3.9']],
                ['11.0'  , ''       , [jsr305: '1.3.9']],
                ['10.0.1', ''       , [jsr305: '1.3.9']],
                ['10.0'  , ''       , [jsr305: '1.3.9']]
        ]
    }

    static allGuavaCombinations(boolean useVersionForEnv) {
        def result = []
        allGuavaVersions().each {
            int majorGuavaVersion = it[0].substring(0, 2) as Integer
            if (useVersionForEnv || majorGuavaVersion < 31) {
                result.add([it[0], it[1], it[2], 'android', 'compileClasspath'])
                result.add([it[0], it[1], it[2], 'android', 'runtimeClasspath'])
                result.add([it[0], it[1], it[2], 'standard-jvm', 'compileClasspath'])
                result.add([it[0], it[1], it[2], 'standard-jvm', 'runtimeClasspath'])
            }
        }
        if (System.getProperty("gradleVersionUnderTest") == "7.5.1") {
            // only do all permutations for one Gradle version
            return result
        }
        // reduced amount of permutations
        return result.subList(0, 32)
    }

    @Unroll
    def "has correct classpath for Guava selected by target environment version #guavaVersion-#versionSuffix, #jvmEnv, #classpath"() {
        given:
        def attr = GRADLE6_TEST? 'Attribute.of("org.gradle.jvm.environment", String::class.java)' : 'org.gradle.api.attributes.java.TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE'
        def attrValue = GRADLE6_TEST? "\"$jvmEnv\"" : "objects.named(\"$jvmEnv\")"
        buildFile << """
            plugins {
                id("java-library")
                id("org.gradlex.jvm-ecosystem-conflict-resolution")
            }

            repositories {
                mavenCentral()
            }

            val envAttribute = $attr
            configurations.compileClasspath {
                attributes.attribute(envAttribute, $attrValue)
            }
            configurations.runtimeClasspath {
                attributes.attribute(envAttribute, $attrValue)
            }
            
            dependencies.components {
                withModule<org.gradlex.javaecosystem.conflict.resolution.rules.GuavaComponentRule>("com.google.guava:guava")
            }

            dependencies {
                api("com.google.collections:google-collections:1.0")
                api("com.google.guava:listenablefuture:1.0")
                api("com.google.guava:guava:$guavaVersion${versionSuffix ? '-' : ''}$versionSuffix")
            }

            tasks.register("printJars") {
                doLast {
                    configurations.${classpath}.get().files.forEach { println(it.name) }
                }
            }
        """

        expect:
        expectedClasspath(guavaVersion, jvmEnv, classpath, dependencyVersions) == printJars().output.split('\n') as Set

        where:
        [guavaVersion, versionSuffix, dependencyVersions, jvmEnv, classpath] << allGuavaCombinations(true)
    }

    Set<String> expectedClasspath(String guavaVersion, String jvmEnv, String classpath, Map<String, String> dependencyVersions) {
        int majorGuavaVersion = guavaVersion.substring(0, 2) as Integer
        String jarSuffix = majorGuavaVersion < 22 ? '' : jvmEnv == 'android' ? 'android' : (guavaVersion == '22.0' || guavaVersion == '23.0') ? '' : 'jre'
        Set<String> result = ["guava-${guavaVersion}${jarSuffix? '-' : ''}${jarSuffix}.jar"]
        if (dependencyVersions.failureaccess) {
            result += "failureaccess-${dependencyVersions.failureaccess}.jar"
        }
        if (classpath == 'compileClasspath') {
            if (classpath == 'compileClasspath' && dependencyVersions.j2objc) {
                result += "j2objc-annotations-${dependencyVersions.j2objc}.jar"
            }
            if (dependencyVersions.jsr305) {
                result += "jsr305-${dependencyVersions.jsr305}.jar"
            }
            if (dependencyVersions.errorProne) {
                result += "error_prone_annotations-${dependencyVersions.errorProne}.jar"
            }
            if (dependencyVersions.checker && dependencyVersions.checkerCompat) {
                if (jvmEnv == 'android') {
                    result += "checker-compat-qual-${dependencyVersions.checkerCompat}.jar"
                    if (majorGuavaVersion > 30) {
                        result += "checker-qual-${dependencyVersions.checker}.jar"
                    }
                } else {
                    result += "checker-qual-${dependencyVersions.checker}.jar"
                }
            } else if (dependencyVersions.checker) {
                result += "checker-qual-${dependencyVersions.checker}.jar"
            } else if (dependencyVersions.checkerCompat) {
                result += "checker-compat-qual-${dependencyVersions.checkerCompat}.jar"
            }
        }
        return result
    }
}