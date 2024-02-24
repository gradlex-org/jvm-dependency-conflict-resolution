import buildparameters.BuildParametersExtension

pluginManagement {
    includeBuild("gradle/plugins")
}

plugins {
    id("com.gradle.enterprise") version "3.16.1"
    id("gradlexbuild.build-parameters")
}

rootProject.name = "java-ecosystem-capabilities"

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
