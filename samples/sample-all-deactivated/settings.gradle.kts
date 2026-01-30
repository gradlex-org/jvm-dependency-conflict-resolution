pluginManagement {
    // This is for testing against the latest version of the plugin, remove if you copied this for a real project
    if (extra.properties["pluginLocation"] != null) {
        includeBuild(extra.properties["pluginLocation"]!!)
    } else {
        includeBuild("../../")
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.jzy3d.org/releases") {
            mavenContent {
                includeModule("org.jzy3d", "jGL")
                includeModule("org.jzy3d", "jzy3d-jGL")
                includeModule("org.jzy3d", "jzy3d-jGL-awt")
            }
        }
        maven("https://maven.scijava.org/content/groups/public") {
            mavenContent {
                includeGroup("org.jzy3d")
                includeGroup("org.smurn")
            }
        }
        mavenCentral() {
            metadataSources.artifact() // woodstox/wstx-lgpl/3.2.7
        }
    }
}

rootProject.name = "sample-all-deactivated"
