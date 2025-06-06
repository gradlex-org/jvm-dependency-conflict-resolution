package org.gradlex.jvm.dependency.conflict.test.issues

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Issue
import spock.lang.Specification

@Issue("https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/243")
class Issue243Test extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def "works for older versions"() {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-detection")
                id("java-library")
            }
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                implementation("jakarta.xml.ws:jakarta.xml.ws-api:3.0.1")
                implementation("jakarta.jws:jakarta.jws-api:3.0.0")
            }
        """

        expect:
        dependenciesCompile().output.contains '''
compileClasspath - Compile classpath for source set 'main'.
+--- jakarta.xml.ws:jakarta.xml.ws-api:3.0.1
|    +--- jakarta.xml.bind:jakarta.xml.bind-api:3.0.1
|    |    \\--- com.sun.activation:jakarta.activation:2.0.1
|    +--- jakarta.xml.soap:jakarta.xml.soap-api:2.0.1
|    |    \\--- com.sun.activation:jakarta.activation:2.0.1
|    \\--- jakarta.jws:jakarta.jws-api:3.0.0
\\--- jakarta.jws:jakarta.jws-api:3.0.0
'''
    }
}
