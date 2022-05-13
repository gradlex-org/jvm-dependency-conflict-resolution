plugins {
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.0.0-rc-1"
}

group = "de.jjohannes.gradle"
version = "0.4"

gradlePlugin {
    plugins.create(project.name) {
        id = "de.jjohannes.${project.name}"
        implementationClass = "${project.group}.javaecosystem.capabilities.JavaEcosystemCapabilitiesPlugin"
        displayName = "Adds Capabilities to well-known Components hosted on Maven Central."
        description = "Adds Capabilities to well-known Components hosted on Maven Central."
    }
}

pluginBundle {
    website = "https://github.com/jjohannes/java-ecosystem-capabilities"
    vcsUrl = "https://github.com/jjohannes/java-ecosystem-capabilities.git"
    tags = listOf("capabilities", "java",
        "asm", "cglib", "dom4j", "javax", "jakarta", "velocity", "hamcrest", "stax", "woodstox")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

dependencies {
    testImplementation("org.gradle.exemplar:samples-check:1.0.0")
}

tasks.test {
    inputs.dir(layout.projectDirectory.dir("../samples"))
}
