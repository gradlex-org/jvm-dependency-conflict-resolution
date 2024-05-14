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

if (the<BuildParametersExtension>().ci) {
    gradleEnterprise {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}
