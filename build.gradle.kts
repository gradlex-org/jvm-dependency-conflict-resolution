plugins {
    id("groovy")
    id("gradlexbuild.java-ecosystem-capabilities-documentation")
    id("org.gradlex.internal.plugin-publish-conventions") version "0.4"
}

group = "de.jjohannes.gradle"
version = "0.8"

signing {
    isRequired = false
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11)) // to run tests that use Android with 11
}
tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}

pluginPublishConventions {
    id("de.jjohannes.${project.name}")
    implementationClass("de.jjohannes.gradle.javaecosystem.capabilities.JavaEcosystemCapabilitiesPlugin")
    displayName("Java Ecosystem Capabilities Gradle Plugin")
    description("!!! Plugin ID changed to 'org.gradlex.${project.name}' !!!")
    tags("capabilities", "java",
         "asm", "cglib", "commons-io", "dom4j", "guava", "hamcrest", "javax", "jakarta", "junit", "postgresql",
         "stax", "velocity", "woodstox")
    gitHub("https://github.com/gradlex-org/java-ecosystem-capabilities")
    website("https://github.com/gradlex-org/java-ecosystem-capabilities")
    developer {
        id.set("britter")
        name.set("Benedikt Ritter")
        email.set("benedikt@gradlex.org")
    }
    developer {
        id.set("jjohannes")
        name.set("Jendrik Johannes")
        email.set("jendrik@gradlex.org")
    }
}

dependencies {
    implementation("org.gradlex:${project.name}:1.0")

    testImplementation("org.gradle.exemplar:samples-check:1.0.0")
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 4
    inputs.dir(layout.projectDirectory.dir("samples"))
}

val checkAllVersions = tasks.register("checkAllVersions") {
    dependsOn(tasks.check)
}

listOf("6.0.1", "6.4.1", "6.9.3", "7.0.2", "7.1.1", "7.2", "7.3.3", "7.4.2", "7.5.1").forEach { gradleVersionUnderTest ->
    val testGradle = tasks.register<Test>("testGradle$gradleVersionUnderTest") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
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