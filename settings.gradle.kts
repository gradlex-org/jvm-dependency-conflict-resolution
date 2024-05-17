import buildparameters.BuildParametersExtension

pluginManagement {
    includeBuild("gradle/plugins")
}

plugins {
    id("com.gradle.develocity") version "3.17.3"
    id("gradlexbuild.build-parameters")
}

rootProject.name = "jvm-dependency-conflict-resolution"

dependencyResolutionManagement {
    repositories.mavenCentral()
}

develocity {
    buildScan {
        if (buildParameters.ci) {
            termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
            termsOfUseAgree = "yes"
        } else {
            publishing.onlyIf { false }
        }
    }
}
