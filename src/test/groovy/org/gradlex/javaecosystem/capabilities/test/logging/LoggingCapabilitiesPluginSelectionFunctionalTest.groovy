package org.gradlex.javaecosystem.capabilities.test.logging

import org.gradle.util.GradleVersion
import spock.lang.IgnoreIf
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class LoggingCapabilitiesPluginSelectionFunctionalTest extends AbstractLoggingCapabilitiesPluginFunctionalTest {

    @Unroll
    // Looks like a regression in Gradle 6.7+: https://github.com/ljacomet/logging-capabilities/issues/20
    @IgnoreIf({ instance.testGradleVersion >= GradleVersion.version("6.7") })
    def "can select logback in case of conflict (with extra #additional)"() {
        given:
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }

            loggingCapabilities {
                selectSlf4JBinding("ch.qos.logback:logback-classic:1.2.3")
            }

            dependencies {
                runtimeOnly("org.slf4j:slf4j-simple:1.7.27")
${additional.collect { "                runtimeOnly(\"$it\")" }.join("\n")}                
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS
        result.output.contains("logback-classic-1.2.3.jar")

        where:
        additional << [[], ["org.slf4j:slf4j-log4j12:1.7.27"], ["org.slf4j:slf4j-jcl:1.7.27", "org.slf4j:slf4j-log4j12:1.7.27"]]
    }

    def "fail to select logback in case of conflict but logback not available"() {
        given:
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }
            
            loggingCapabilities {
                selectSlf4JBinding("ch.qos.logback:logback-classic:1.2.3")
            }


            dependencies {
                runtimeOnly("org.slf4j:slf4j-simple:1.7.27")
                runtimeOnly("org.slf4j:slf4j-log4j12:1.7.27")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        result.output.contains("conflict on capability 'org.gradlex.logging:slf4j-impl:1.0'")
    }

    @Unroll
    def "configuring logger leaves no open conflicts (using #prefered)"() {
        given:
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }
            
            loggingCapabilities {
                $prefered
            }
            
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.27")
                implementation("org.apache.logging.log4j:log4j-api:2.12.1")
                
                implementation("log4j:log4j:1.2.17")
                implementation("commons-logging:commons-logging:1.2")
                
                runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
                runtimeOnly("org.slf4j:slf4j-simple:1.7.27")
                runtimeOnly("org.slf4j:slf4j-log4j12:1.7.27")
                runtimeOnly("org.slf4j:log4j-over-slf4j:1.7.27")
                runtimeOnly("org.slf4j:jul-to-slf4j:1.7.27")
                runtimeOnly("org.slf4j:slf4j-jdk14:1.7.27")
                runtimeOnly("org.slf4j:slf4j-jcl:1.7.27")
                runtimeOnly("org.slf4j:jcl-over-slf4j:1.7.27")

                runtimeOnly("org.apache.logging.log4j:log4j-jul:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-1.2-api:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-to-slf4j:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-jcl:2.12.1")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS
        result.output.contains(selected)

        where:
        prefered               | selected
        "enforceLogback()"     | "logback-classic-1.2.3.jar"
        "enforceSlf4JSimple()" | "slf4j-simple-1.7.27.jar"
        "enforceLog4J2()"      | "log4j-slf4j-impl-2.12.1.jar"
    }

    @Unroll
    def "Enforcing a logger leaves no open conflict when enforcing #enforced and #extra is present"() {
        given:
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }
            
            loggingCapabilities {
                ${loggerFrom(enforced)}
            }

            dependencies {
                runtimeOnly("$enforced")
                runtimeOnly("$extra")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS

        where:
        [enforced, extra] << [["ch.qos.logback:logback-classic:1.2.3", "org.slf4j:slf4j-simple:1.7.27", "org.apache.logging.log4j:log4j-slf4j-impl:2.12.1"],
                               ["log4j:log4j:1.2.17", "commons-logging:commons-logging:1.2", "org.apache.logging.log4j:log4j-slf4j-impl:2.12.1", "ch.qos.logback:logback-classic:1.2.3",
                                "org.slf4j:slf4j-simple:1.7.27", "org.slf4j:slf4j-log4j12:1.7.27", "org.slf4j:log4j-over-slf4j:1.7.27", "org.slf4j:jul-to-slf4j:1.7.27", "org.slf4j:slf4j-jdk14:1.7.27",
                                "org.slf4j:slf4j-jcl:1.7.27", "org.slf4j:jcl-over-slf4j:1.7.27", "org.apache.logging.log4j:log4j-jul:2.12.1", "org.apache.logging.log4j:log4j-1.2-api:2.12.1",
                                "org.apache.logging.log4j:log4j-to-slf4j:2.12.1", "org.apache.logging.log4j:log4j-jcl:2.12.1", "org.apache.logging.log4j:log4j-core:2.12.1"]].combinations()
    }

    String loggerFrom(String enforced) {
        if (enforced.contains('logback-classic')) {
            return 'enforceLogback()'
        } else if (enforced.contains('slf4j-simple')) {
            return 'enforceSlf4JSimple()'
        } else if (enforced.contains('log4j-slf4j-impl')) {
            return 'enforceLog4J2()'
        }
        throw new IllegalArgumentException("Unexpected value: $enforced")
    }


    def "can enforce logback and commons-logging is substituted"() {
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }
            
            loggingCapabilities {
                enforceLogback()
            }
            
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.27")
                
                implementation("commons-logging:commons-logging:1.2")
                
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS
        !result.output.contains("commons-logging-1.2.jar")
        result.output.contains("jcl-over-slf4j-1.7.27.jar")
    }

    def "can enforce logback and spring-jcl is substituted"() {
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }

            loggingCapabilities {
                enforceLogback()
            }

            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.27")

                implementation("org.springframework:spring-jcl:5.3.9")

                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS
        !result.output.contains("spring-jcl-5.3.9.jar")
        result.output.contains("jcl-over-slf4j-1.7.27.jar")
    }

    def "can enforce different loggers in runtime and test"() {
        given:
        withBuildScript("""
            plugins {
                `java-library`
                id("org.gradlex.logging-capabilities")
            }

            repositories {
                mavenCentral()
            }
            
            loggingCapabilities {
                enforceLog4J2("runtimeClasspath")
                enforceSlf4JSimple("testRuntimeClasspath")
            }
            
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.27")
                implementation("org.apache.logging.log4j:log4j-api:2.12.1")
                
                implementation("log4j:log4j:1.2.17")
                implementation("commons-logging:commons-logging:1.2")
                
                runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
                runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
                runtimeOnly("org.slf4j:slf4j-simple:1.7.27")
                runtimeOnly("org.slf4j:slf4j-log4j12:1.7.27")
                runtimeOnly("org.slf4j:log4j-over-slf4j:1.7.27")
                runtimeOnly("org.slf4j:jul-to-slf4j:1.7.27")
                runtimeOnly("org.slf4j:slf4j-jdk14:1.7.27")
                runtimeOnly("org.slf4j:slf4j-jcl:1.7.27")
                runtimeOnly("org.slf4j:jcl-over-slf4j:1.7.27")

                runtimeOnly("org.apache.logging.log4j:log4j-jul:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-1.2-api:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-to-slf4j:2.12.1")
                runtimeOnly("org.apache.logging.log4j:log4j-jcl:2.12.1")
            }

            tasks.register("doIt") {
                doLast {
                    println(configurations["runtimeClasspath"].files)
                    println(configurations["testRuntimeClasspath"].files)
                }
            }
""")
        when:
        def result = build(['doIt'])

        then:
        outcomeOf(result, ':doIt') == SUCCESS
    }
}
