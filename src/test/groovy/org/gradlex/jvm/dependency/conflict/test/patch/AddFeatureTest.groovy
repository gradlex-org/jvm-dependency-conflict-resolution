package org.gradlex.jvm.dependency.conflict.test.patch

class AddFeatureTest extends AbstractPatchTest {

    def "can add feature"() {
        given:
        buildFile << """
            jvmDependencyConflicts {
                patch {
                    module("io.netty:netty-transport-native-epoll") {
                        addFeature("linux-x86_64")
                        addFeature("linux-aarch_64")
                    }
                }
            }
            dependencies {
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final")
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final") {
                    capabilities { requireCapabilities("io.netty:netty-transport-native-epoll-linux-x86_64") }
                }
                implementation("io.netty:netty-transport-native-epoll:4.1.106.Final") {
                    capabilities { requireCapabilities("io.netty:netty-transport-native-epoll-linux-aarch_64") }
                }
            }
        """

        expect:
        def output = dependencyInsight('io.netty:netty-transport-native-epoll').output
        output.contains 'linux-x86_64Compile'
        output.contains 'linux-aarch_64Compile'
    }
}
