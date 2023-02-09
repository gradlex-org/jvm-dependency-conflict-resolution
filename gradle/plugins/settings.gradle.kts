dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("build-parameters")
include("documentation")

if (!JavaVersion.current().isJava11Compatible) {
    error("This build requires JDK 11 or higher") // The custom build logic that loads classes (e.g. ReadmeUpdate) fails otherwise
}
