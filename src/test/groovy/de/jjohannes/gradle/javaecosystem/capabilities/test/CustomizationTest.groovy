package de.jjohannes.gradle.javaecosystem.capabilities.test

import de.jjohannes.gradle.javaecosystem.capabilities.test.fixture.GradleBuild
import spock.lang.Specification

class CustomizationTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def "can revert effect of a rule by adding another rule"() {
        given:
        buildFile << """
            import de.jjohannes.gradle.javaecosystem.capabilities.rules.CGlibRule
            
            plugins {
                id("de.jjohannes.java-ecosystem-capabilities")
                id("java-library")
            }
            
            repositories.mavenCentral()
            
            javaEcosystemCapabilities {
                deactivatedResolutionStrategies.addAll(allCapabilities)
            }
            
            dependencies {
                implementation("cglib:cglib-nodep:3.2.10")
                implementation("cglib:cglib:3.2.10")
            
                components.withModule(CGlibRule.MODULES[0]) {
                    allVariants {
                        withCapabilities {
                            removeCapability(CGlibRule.CAPABILITY_GROUP, CGlibRule.CAPABILITY_NAME)
                        }
                    }
                }
            }
        """

        expect:
        dependencies().output.contains '''
            compileClasspath - Compile classpath for source set 'main'.
            +--- cglib:cglib-nodep:3.2.10
            \\--- cglib:cglib:3.2.10
                 +--- org.ow2.asm:asm:7.0
                 \\--- org.apache.ant:ant:1.10.3
                      \\--- org.apache.ant:ant-launcher:1.10.3
        '''.stripIndent()
    }

    def "can use own capability resolution strategies"() {
        given:
        buildFile << """
            plugins {
                id("de.jjohannes.java-ecosystem-capabilities")
                id("java-library")
            }
            
            repositories.mavenCentral()
            
            configurations.all {
                resolutionStrategy.capabilitiesResolution {
                    withCapability("cglib:cglib") {
                        select("cglib:cglib:0")
                    }
                }
                resolutionStrategy.capabilitiesResolution {
                    withCapability("javax.mail:mail") {
                       select("com.sun.mail:jakarta.mail:0")
                    }
                }
                resolutionStrategy.capabilitiesResolution {
                    withCapability("javax.ws.rs:jsr311-api") {
                        select("org.jboss.resteasy:jaxrs-api:0")
                    }
                }
                resolutionStrategy.capabilitiesResolution {
                    withCapability("jakarta.servlet:jakarta.servlet-api") {
                        select("jakarta.servlet:jakarta.servlet-api:0")
                    }
                }
            }
            
            dependencies {
                implementation("cglib:cglib-nodep:3.2.10")
                implementation("cglib:cglib:3.2.10")
                implementation("com.sun.mail:jakarta.mail:1.6.7")
                implementation("com.sun.mail:mailapi:1.6.7")
                implementation("jakarta.servlet:jakarta.servlet-api:4.0.4")
                implementation("org.apache.tomcat:tomcat-servlet-api:10.0.18")
            }
        """

        expect:
        dependencies().output.contains '''
            compileClasspath - Compile classpath for source set 'main'.
            +--- cglib:cglib-nodep:3.2.10 -> cglib:cglib:3.2.10
            |    +--- org.ow2.asm:asm:7.0
            |    \\--- org.apache.ant:ant:1.10.3
            |         \\--- org.apache.ant:ant-launcher:1.10.3
            +--- cglib:cglib:3.2.10 (*)
            +--- com.sun.mail:jakarta.mail:1.6.7
            |    \\--- com.sun.activation:jakarta.activation:1.2.1
            +--- com.sun.mail:mailapi:1.6.7 -> com.sun.mail:jakarta.mail:1.6.7 (*)
            +--- jakarta.servlet:jakarta.servlet-api:4.0.4
            \\--- org.apache.tomcat:tomcat-servlet-api:10.0.18 -> jakarta.servlet:jakarta.servlet-api:4.0.4
        '''.stripIndent()
    }
}
