package org.gradlex.jvm.dependency.conflict.test.patch

class SetStatusTest extends AbstractPatchTest {
    def "can set component status to integration for certain versions"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("com.fasterxml.jackson.core:jackson-core") {
                        setStatusToIntegration("-m", "-rc")
                    }
                }
            }
            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-core:2.15.0-rc3")
            }
        """

        expect:
        dependencyInsight("com.fasterxml.jackson.core:jackson-core").output.contains 'integration'
    }
}
