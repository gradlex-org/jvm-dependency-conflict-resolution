plugins {
    id("org.gradlex.build-parameters") version "1.4.3"
}

buildParameters {
    pluginId("gradlexbuild.build-parameters")
    bool("ci") {
        fromEnvironment()
        defaultValue = false
    }
}
