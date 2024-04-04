package org.gradlex.javaecosystem.conflict.test

import org.gradlex.javaecosystem.conflict.test.fixture.GradleBuild
import spock.lang.IgnoreIf
import spock.lang.Specification

@IgnoreIf({ GradleBuild.GRADLE6_TEST || GradleBuild.GRADLE7_TEST })
class ComponentStatusRuleTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        buildFile << """
            import org.gradlex.javaecosystem.conflict.resolution.rules.*
            plugins {
                id("org.gradlex.jvm-ecosystem-conflict-resolution")
                id("java-library")
            }
            repositories.mavenCentral()
            dependencies.components {
                all<ComponentStatusRule>() { params(listOf(
                    "-b", "alpha", "beta", "cr", "m", "rc"
                ))}
            }
        """
    }

    def "status of non-released versions is set to integration"() {
        given:
        buildFile << """
            dependencies {
                implementation("com.sun.activation:jakarta.activation:2.0.0-rc1")
                implementation("com.sun.mail:jakarta.mail:2.0.0-RC6")
                implementation("jakarta.servlet:jakarta.servlet-api:5.0.0-M2")
                implementation("org.jboss.resteasy:resteasy-client:5.0.0.Beta3")
                implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
            }
        """

        expect:
        status('com.sun.activation:jakarta.activation') == 'integration'
        status('com.sun.mail:jakarta.mail') == 'integration'
        status('jakarta.servlet:jakarta.servlet-api') == 'integration'
        status('org.jboss.resteasy:resteasy-client') == 'integration'
        status('org.slf4j:slf4j-api') == 'integration'
    }

    def "status of released versions stays release"() {
        given:
        buildFile << """
            dependencies {
                implementation("com.sun.activation:jakarta.activation:2.0.0")
                implementation("com.sun.mail:jakarta.mail:2.0.0")
                implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
                implementation("org.jboss.resteasy:resteasy-client:5.0.0.Final")
                implementation("org.slf4j:slf4j-api:2.0.0")
            }
        """

        expect:
        status('com.sun.activation:jakarta.activation') == 'release'
        status('com.sun.mail:jakarta.mail') == 'release'
        status('jakarta.servlet:jakarta.servlet-api') == 'release'
        status('org.jboss.resteasy:resteasy-client') == 'release'
        status('org.slf4j:slf4j-api') == 'release'
    }

    private String status(String module) {
        dependencyInsight(module).output.readLines().find { it.contains('org.gradle.status') }.split('\\|')[2].trim()
    }
}
