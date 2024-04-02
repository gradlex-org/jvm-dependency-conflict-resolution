package org.gradlex.javaecosystem.capabilities.test

import org.gradlex.javaecosystem.capabilities.test.fixture.GradleBuild
import spock.lang.Specification

class CustomizationTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def "can revert effect of a rule by adding another rule"() {
        given:
        buildFile << """
            import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions
            
            plugins {
                id("org.gradlex.java-ecosystem-capabilities")
                id("java-library")
            }
            
            repositories.mavenCentral()
            
            dependencies {
                implementation("cglib:cglib-nodep:3.2.10")
                implementation("cglib:cglib:3.2.10")
            
                components.withModule(CapabilityDefinitions.CGLIB.modules[0]) {
                    allVariants {
                        withCapabilities {
                            removeCapability(CapabilityDefinitions.CGLIB.group, CapabilityDefinitions.CGLIB.capabilityName)
                        }
                    }
                }
            }
        """

        expect:
        dependenciesCompile().output.contains '''
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
            import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions
            
            plugins {
                id("org.gradlex.java-dependencies")
                id("java-library")
            }
            
            repositories.mavenCentral()
            
            javaDependencies {
                conflictResolution.deactivatedResolutionStrategies.add(CapabilityDefinitions.CGLIB)
                conflictResolution.deactivatedResolutionStrategies.add(CapabilityDefinitions.JAVAX_MAIL_API)
                conflictResolution.deactivatedResolutionStrategies.add(CapabilityDefinitions.JAVAX_WS_RS_API)
                conflictResolution.deactivatedResolutionStrategies.add(CapabilityDefinitions.JAKARTA_SERVLET_API)
            }
            
            configurations.all {
                resolutionStrategy.capabilitiesResolution {
                    withCapability(CapabilityDefinitions.CGLIB.capability) {
                        select("cglib:cglib:0")
                    }
                    withCapability(CapabilityDefinitions.JAVAX_MAIL_API.capability) {
                       select("com.sun.mail:jakarta.mail:0")
                    }
                    withCapability(CapabilityDefinitions.JAVAX_WS_RS_API.capability) {
                        select("org.jboss.resteasy:jaxrs-api:0")
                    }
                    withCapability(CapabilityDefinitions.JAKARTA_SERVLET_API.capability) {
                        select("jakarta.servlet:jakarta.servlet-api:0")
                    }
                }
            }
            
            dependencies {
                implementation("cglib:cglib-nodep:3.2.10")
                implementation("cglib:cglib:3.2.10")
                implementation("com.sun.mail:jakarta.mail:1.6.7")
                implementation("com.sun.mail:mailapi:1.6.7")
                implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
                implementation("org.apache.tomcat:tomcat-servlet-api:10.0.18")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
            compileClasspath - Compile classpath for source set 'main'.
            +--- cglib:cglib-nodep:3.2.10 -> cglib:cglib:3.2.10
            |    +--- org.ow2.asm:asm:7.0
            |    \\--- org.apache.ant:ant:1.10.3
            |         \\--- org.apache.ant:ant-launcher:1.10.3
            +--- cglib:cglib:3.2.10 (*)
            +--- com.sun.mail:jakarta.mail:1.6.7
            |    \\--- com.sun.activation:jakarta.activation:1.2.1
            +--- com.sun.mail:mailapi:1.6.7 -> com.sun.mail:jakarta.mail:1.6.7 (*)
            +--- jakarta.servlet:jakarta.servlet-api:5.0.0
            \\--- org.apache.tomcat:tomcat-servlet-api:10.0.18 -> jakarta.servlet:jakarta.servlet-api:5.0.0
        '''.stripIndent()
    }
}
