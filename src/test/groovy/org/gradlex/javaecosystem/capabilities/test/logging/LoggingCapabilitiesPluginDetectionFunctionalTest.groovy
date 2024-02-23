/*
 * Copyright the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.javaecosystem.capabilities.test.logging

import org.gradle.util.GradleVersion
import spock.lang.Requires
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.FAILED

class LoggingCapabilitiesPluginDetectionFunctionalTest extends AbstractLoggingCapabilitiesPluginFunctionalTest {

    boolean conflictOnCapability(String output, String capability) {
        // Error became lenient in Gradle 5.3, with a different error message:
        // https://github.com/gradle/gradle/commit/0c10062f9c86192b2568b0035ec8885c75b024cc
        return output.contains("conflict on capability '$capability'") || // Gradle >= 5.3
               output.contains("provide the same capability: $capability") // Gradle <= 5.2
    }

    @Unroll
    def "can detect Slf4J logger implementation conflicts with #first and #second"() {
        given:
        withBuildScriptWithDependencies(first, second)

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex.logging:slf4j-impl:1.0")

        where:
        first                           | second
        'org.slf4j:slf4j-simple:1.7.27' | 'ch.qos.logback:logback-classic:1.2.3'
        'org.slf4j:slf4j-simple:1.7.27' | 'org.slf4j:slf4j-log4j12:1.7.27'
        'org.slf4j:slf4j-simple:1.7.27' | 'org.slf4j:slf4j-jcl:1.7.27'
        'org.slf4j:slf4j-simple:1.7.27' | 'org.slf4j:slf4j-jdk14:1.7.27'
        'org.slf4j:slf4j-simple:1.7.27' | 'org.apache.logging.log4j:log4j-slf4j-impl:2.17.0'
        'org.slf4j:slf4j-simple:1.7.27' | 'org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0'

    }

    @Unroll
    def "can detect Slf4J logger implementation / bridge implementation conflicts with #first and #second"() {
        given:
        withBuildScriptWithDependencies(first, second)

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex.logging:$capability:1.7.27")

        where:
        first                               | second                            | capability
        'org.slf4j:jcl-over-slf4j:1.7.27'   | 'org.slf4j:slf4j-jcl:1.7.27'      | 'slf4j-vs-jcl'
        'org.slf4j:jul-to-slf4j:1.7.27'     | 'org.slf4j:slf4j-jdk14:1.7.27'    | 'slf4j-vs-jul'
        'org.slf4j:log4j-over-slf4j:1.7.27' | 'org.slf4j:slf4j-log4j12:1.7.27'  | 'slf4j-vs-log4j'
    }

    @Unroll
    def "can detect Slf4J bridge implementations vs native logger implementations with #first and #second"() {
        given:
        withBuildScriptWithDependencies(first, second)

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex.logging:$capability:1.0")

        where:
        first                                           | second                                            | capability
        'org.slf4j:jcl-over-slf4j:1.7.27'               | 'commons-logging:commons-logging:1.2'             | 'commons-logging-impl'
        'org.springframework:spring-jcl:5.3.9'          | 'commons-logging:commons-logging:1.2'             | 'commons-logging-impl'
        'org.slf4j:log4j-over-slf4j:1.7.27'             | 'log4j:log4j:1.2.9'                               | 'slf4j-vs-log4j2-log4j'
        'org.slf4j:log4j-over-slf4j:1.7.27'             | 'org.apache.logging.log4j:log4j-1.2-api:2.17.0'   | 'slf4j-vs-log4j2-log4j'
        'org.slf4j:slf4j-log4j12:1.7.27'                | 'org.apache.logging.log4j:log4j-1.2-api:2.17.0'   | 'slf4j-vs-log4j2-log4j'
        'org.apache.logging.log4j:log4j-1.2-api:2.17.0' | 'org.slf4j:slf4j-log4j12:1.7.27'                  | 'slf4j-vs-log4j2-log4j'
    }

    @Unroll
    def "can detect Log4J2 logger implementation / bridge implementation conflict with #bridge"() {
        given:
        withBuildScriptWithDependencies(bridge, 'org.apache.logging.log4j:log4j-to-slf4j:2.20.0')

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex.logging:log4j2-vs-slf4j:2.20.0")

        where:
        bridge << ['org.apache.logging.log4j:log4j-slf4j-impl:2.20.0', 'org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0']
    }

    def "can detect Log4J2 logger implementation conflict"() {
        given:
        withBuildScriptWithDependencies('org.apache.logging.log4j:log4j-core:2.17.0', 'org.apache.logging.log4j:log4j-to-slf4j:2.17.0')

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex:log4j2-impl:2.17.0")
    }

    @Unroll
    def "can detect conflicting bridge implementations from Slf4J and Log4J2 with #first and #second"() {
        given:
        withBuildScriptWithDependencies(first, second)

        when:
        def result = buildAndFail(['doIt'])

        then:
        outcomeOf(result, ':doIt') == FAILED
        conflictOnCapability(result.output, "org.gradlex.logging:$capability:1.0")

        where:
        first                               | second                                        | capability
        'org.slf4j:jul-to-slf4j:1.7.27'     | 'org.apache.logging.log4j:log4j-jul:2.17.0'   | 'slf4j-vs-log4j2-jul'
        'org.slf4j:jcl-over-slf4j:1.7.27'   | 'org.apache.logging.log4j:log4j-jcl:2.17.0'   | 'slf4j-vs-log4j2-jcl'
    }

    def "provides alignment on Slf4J"() {
        given:
        withBuildScriptWithDependencies("org.slf4j:slf4j-simple:1.7.25", "org.slf4j:slf4j-api:1.7.27")

        when:
        def result = build(['doIt'])

        then:
        result.output.contains("slf4j-simple-1.7.27.jar")
    }

    def "provides alignment on Log4J 2"() {
        given:
        withBuildScriptWithDependencies("org.apache.logging.log4j:log4j-to-slf4j:2.17.0", "org.apache.logging.log4j:log4j-api:2.16.0")

        when:
        def result = build(['doIt'])

        then:
        result.output.contains("log4j-to-slf4j-2.17.0.jar")
    }
}
