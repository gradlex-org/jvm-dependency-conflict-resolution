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

package org.gradlex.javaecosystem.conflict.test

import org.gradlex.javaecosystem.conflict.test.fixture.GradleBuild
import spock.lang.Specification
import spock.lang.Tag

@Tag("no-cross-version")
class RulesModeTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def "applying the plugin in a project without rulesMode configured works"() {
        given:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        expect:
        build()
    }

    def "applying the plugin in a project with rulesMode set to FAIL_ON_PROJECT_RULES fails the build"() {
        given:
        settingsFile << """
            dependencyResolutionManagement {
                rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
            }
        """

        and:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        when:
        def result = fail()

        then:
        result.output.contains("RulesMode is set to FAIL_ON_PROJECT_RULES")
        result.output.contains("As a result this plugin will not work.")
        result.output.contains("Fix this problem by either changing dependencyResolutionManagement.rulesMode to PREFER_PROJECT or by applying 'org.gradlex.jvm-dependency-conflict-detection' as a settings plugin")
    }

    def "applying the plugin in a project and applying the base plugin in settings with rulesMode set to FAIL_ON_PROJECT_RULES works"() {
        given:
        settingsFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-detection")
            }

            dependencyResolutionManagement {
                rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
            }
        """

        and:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        expect:
        build()
    }

    def "applying the plugin in a project without applying the base plugin in settings and rulesMode set to PREFER_SETTINGS fails the build"() {
        given:
        settingsFile << """
            dependencyResolutionManagement {
                rulesMode = RulesMode.PREFER_SETTINGS
            }
        """

        and:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        when:
        def result = fail()

        then:
        result.output.contains("RulesMode is set to PREFER_SETTINGS")
        result.output.contains("As a result this plugin will not work.")
        result.output.contains("Fix this problem by either changing dependencyResolutionManagement.rulesMode to PREFER_PROJECT or by applying 'org.gradlex.jvm-dependency-conflict-detection' as a settings plugin")
    }

    def "applying the plugin in a project and applying the base plugin in settings and rulesMode set to PREFER_SETTINGS works"() {
        given:
        settingsFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-detection")
            }

            dependencyResolutionManagement {
                rulesMode = RulesMode.PREFER_SETTINGS
            }
        """

        and:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        expect:
        build()
    }

    def "applying the plugin in a project with rulesMode set to PREFER_PROJECT works, because the base plugin is applied to the project"() {
        given:
        settingsFile << """
            dependencyResolutionManagement {
                rulesMode = RulesMode.PREFER_PROJECT
            }
        """

        and:
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
            }
        """

        expect:
        build()
    }
}
