pluginManagement {
    // This is for testing against the latest version of the plugin, remove if you copied this for a real project
    if (extra.properties["pluginLocation"] != null) {
        includeBuild(extra.properties["pluginLocation"]!!)
    } else {
        includeBuild("../../")
    }
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}
