pluginManagement {
    includeBuild("plugin")
    includeBuild("samples/sample-all")
    includeBuild("samples/sample-own-resolution-rules")
    includeBuild("documentation")
}

plugins {
    id("com.gradle.enterprise") version("3.8.1")
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.6.3"
}

gradleEnterprise {
    buildScan {
        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
