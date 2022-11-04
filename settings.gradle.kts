pluginManagement {
    includeBuild("plugin")
    includeBuild("samples/sample-all")
    includeBuild("samples/sample-own-metadata-rule")
    includeBuild("samples/sample-own-resolution-rules")
    includeBuild("documentation")
}

plugins {
    id("com.gradle.enterprise") version "3.11.2"
}

gradleEnterprise {
    buildScan {
        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
