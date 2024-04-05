package org.gradlex.jvm.dependency.conflict.test

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Specification
import spock.lang.Tag

class SettingsTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    @Tag("no-cross-version")
    def "plugin can be applied in settings file"() {
        given:
        settingsFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-detection")
            }
        """
        buildFile << """
            import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition
            
            plugins {
                id("java-library")
            }
            
            repositories.mavenCentral()
            
            configurations.all {
                resolutionStrategy.capabilitiesResolution {
                    withCapability(CapabilityDefinition.CGLIB.capability) {
                        select("cglib:cglib:0")
                    }
                    withCapability(CapabilityDefinition.JAVAX_MAIL_API.capability) {
                       select("com.sun.mail:jakarta.mail:0")
                    }
                    withCapability(CapabilityDefinition.JAVAX_WS_RS_API.capability) {
                        select("org.jboss.resteasy:jaxrs-api:0")
                    }
                    withCapability(CapabilityDefinition.JAKARTA_SERVLET_API.capability) {
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
