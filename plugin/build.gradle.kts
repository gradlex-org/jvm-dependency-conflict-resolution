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

val checkAllVersions = tasks.register("checkAllVersions") {
    dependsOn(tasks.check)
}

listOf("6.0.1", "6.4.1", "6.9.3", "7.0.2", "7.1.1", "7.2", "7.3.3", "7.4.2", "7.5.1").forEach { gradleVersionUnderTest ->
    val testGradle = tasks.register<Test>("testGradle$gradleVersionUnderTest") {
        group = "verification"
        description = "Runs tests against Gradle $gradleVersionUnderTest"
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath
        useJUnitPlatform {
            excludeTags("no-cross-version")
        }
        maxParallelForks = 4
        systemProperty("gradleVersionUnderTest", gradleVersionUnderTest)

        exclude("**/*SamplesTest.class") // Not yet cross-version ready
    }
    checkAllVersions {
        dependsOn(testGradle)
    }
}