package org.gradlex.javaecosystem.conflict.test

import org.gradlex.javaecosystem.conflict.test.fixture.GradleBuild
import spock.lang.Specification
import spock.lang.Unroll

class AopAllianceMinVersionTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        settingsFile << """
            pluginManagement {
                repositories.mavenCentral()
            }
        """
    }

    @Unroll
    def "include aopalliance for older spring-aop v#springVersion"(String springVersion) {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-ecosystem-conflict-resolution")
                id("java-library")
            }

            repositories {
                mavenCentral()
                google()
            }
            
            dependencies {
                implementation("aopalliance:aopalliance:1.0")
                implementation("org.springframework:spring-aop:${springVersion}")
            }
            
            tasks.register("printJars") {
                println(configurations.compileClasspath.get().files.joinToString("\\n") { it.name });
            }
        """

        expect:
        def jars = printJars().output.trim().split("\n").toList()
        jars.contains("aopalliance-1.0.jar")
        jars.contains("spring-aop-" + springVersion + ".jar")

        where:
        springVersion << [ "4.2.9.RELEASE", "4.1.9.RELEASE", "4.0.9.RELEASE", "3.2.18.RELEASE", "3.1.4.RELEASE", "3.0.7.RELEASE" ]
    }

    @Unroll
    def "exclude aopalliance for older spring-aop v#springVersion"(String springVersion) {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-ecosystem-conflict-resolution")
                id("java-library")
            }

            repositories {
                mavenCentral()
                google()
            }
            
            dependencies {
                implementation("aopalliance:aopalliance:1.0")
                implementation("org.springframework:spring-aop:${springVersion}")
            }
            
            tasks.register("printJars") {
                println(configurations.compileClasspath.get().files.joinToString("\\n") { it.name });
            }
        """

        expect:
        def jars = printJars().output.trim().split("\n").toList()
        !jars.contains("aopalliance-1.0.jar")
        jars.contains("spring-aop-" + springVersion + ".jar")

        where:
        springVersion << [ "4.3.30.RELEASE", "5.0.20.RELEASE", "5.1.20.RELEASE", "5.2.25.RELEASE", "5.3.31" ]
    }
}
