import buildparameters.BuildParametersExtension

pluginManagement {
    includeBuild("gradle/plugins")
}

plugins {
    id("com.gradle.develocity") version "4.1.1"
    id("gradlexbuild.build-parameters")
}

rootProject.name = "jvm-dependency-conflict-resolution"

dependencyResolutionManagement {
    repositories.mavenCentral()
}

develocity {
    buildScan {
        // required until we can upgrade to Gradle 8.8
        val buildParameters = the<BuildParametersExtension>()
        if (buildParameters.ci) {
            termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
            termsOfUseAgree = "yes"
        } else {
            publishing.onlyIf { false }
        }
    }
}
