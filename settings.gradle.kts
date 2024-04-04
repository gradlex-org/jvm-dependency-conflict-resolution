import buildparameters.BuildParametersExtension

pluginManagement {
    includeBuild("gradle/plugins")
}

plugins {
    id("com.gradle.enterprise") version "3.16.2"
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
