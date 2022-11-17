plugins {
    id("org.gradlex.build-parameters") version "1.3"
}

buildParameters {
    pluginId("gradlexbuild.build-parameters")
    bool("ci") {
        fromEnvironment()
        defaultValue.set(false)
    }
}
