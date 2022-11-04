plugins {
    id("groovy")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "de.jjohannes.gradle"
version = "0.6"

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
    toolchain.languageVersion.set(JavaLanguageVersion.of(11)) // to run tests that use Android with 11
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

dependencies {
    testImplementation("org.gradle.exemplar:samples-check:1.0.0")
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4
    inputs.dir(layout.projectDirectory.dir("../samples"))
}
