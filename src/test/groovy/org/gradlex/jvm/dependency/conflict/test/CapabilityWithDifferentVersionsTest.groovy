package org.gradlex.jvm.dependency.conflict.test

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Specification

import static org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild.GRADLE6_TEST

class CapabilityWithDifferentVersionsTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def "does not fail if capability is added several times with different versions"() {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-detection") apply false
                id("java-library")
            }
            repositories.mavenCentral()
            dependencies.components {
                withModule("com.google.guava:guava") {
                    allVariants {
                        withCapabilities {
                            addCapability("com.google.guava", "listenablefuture", 
                                "9999.0-empty-to-avoid-conflict-with-guava")
                        }
                    }
                }
            }
            
            apply(plugin = "org.gradlex.jvm-dependency-conflict-detection")
            
            dependencies {
                api("com.google.guava:guava:33.0.0-jre")
            }

            tasks.register("printJars") {
                println(configurations.compileClasspath.get().files.joinToString("\\n") { it.name })
            }
        """
        if (GRADLE6_TEST) { configureEnvAttribute() }

        expect:
        printJars()
    }

    def "does not fail with empty listenable future dependency on the classpath"() {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
                id("java-library")
            }
            tasks.withType<JavaCompile>().configureEach {
                options.release.set(17)
            }
            repositories.mavenCentral()
            dependencies {
                 implementation(platform("com.google.cloud:spring-cloud-gcp-dependencies:5.2.0"))
                 implementation("com.google.cloud:spring-cloud-gcp-starter-bigquery")
            }

            tasks.register("printJars") {
                println(configurations.compileClasspath.get().files.joinToString("\\n") { it.name })
            }
        """
        if (GRADLE6_TEST) { configureEnvAttribute() }

        expect:
        printJars()
    }

    void configureEnvAttribute() {
        buildFile << """
            val envAttribute = Attribute.of("org.gradle.jvm.environment", String::class.java)
            configurations.compileClasspath {
                attributes.attribute(envAttribute, "standard-jvm")
            }
        """
    }
}
