package org.gradlex.jvm.dependency.conflict.test

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Specification

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

        expect:
        printJars()
    }
}
