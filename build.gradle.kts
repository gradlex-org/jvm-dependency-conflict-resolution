plugins {
    id("groovy")
    id("gradlexbuild.lifecycle")
    id("gradlexbuild.jvm-dependency-conflict-detection-documentation")
    id("org.gradlex.internal.plugin-publish-conventions") version "0.6"
}

group = "org.gradlex"
version = "2.2"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(11) // to run tests that use Android with 11
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 8
}

tasks.withType<Javadoc>().configureEach {
    options {
        this as StandardJavadocDocletOptions
        encoding = "UTF-8"
        addStringOption("Xdoclint:all,-missing", "-quiet")
        addStringOption("Xwerror", "-quiet")
    }
}

val tagsList = listOf(
    "dependency", "dependencies", "dependency-management", "capabilities", "java", "logging",
    "asm", "bouncycastle", "cglib", "commons-io", "dom4j", "guava", "hamcrest", "javax", "jakarta", "junit",
    "mysql", "postgresql", "stax", "slf4j", "log4j2", "velocity", "woodstox"
)

pluginPublishConventions {
    id("org.gradlex.jvm-dependency-conflict-resolution")
    implementationClass("org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin")
    displayName("JVM Conflict Resolution Gradle Plugin")
    description("Convenient dependency conflict management for Java projects.")
    tags(*tagsList.toTypedArray())
    gitHub("https://github.com/gradlex-org/jvm-dependency-conflict-resolution")
    website("https://github.com/gradlex-org/jvm-dependency-conflict-resolution")
    developer {
        id = "britter"
        name = "Benedikt Ritter"
        email = "benedikt@gradlex.org"
    }
    developer {
        id = "jjohannes"
        name = "Jendrik Johannes"
        email = "jendrik@gradlex.org"
    }
    developer {
        id = "ljacomet"
        name = "Louis Jacomet"
        email = "louis@gradlex.org"
    }
}

gradlePlugin {
    plugins {
        create("jvm-dependency-conflict-detection") {
            id = "org.gradlex.jvm-dependency-conflict-detection"
            implementationClass = "org.gradlex.jvm.dependency.conflict.detection.JvmDependencyConflictDetectionPlugin"
            displayName = "JVM Conflict Detection Gradle Plugin"
            description = "Adds Capabilities to well-known Components hosted on Maven Central."
            tags = tagsList
        }
    }
}

dependencies {
    testImplementation("org.gradle.exemplar:samples-check:1.0.2")
    testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

testing.suites.named<JvmTestSuite>("test") {
    useJUnitJupiter()
    listOf("6.8.3", "6.9.4", "7.0.2", "7.6.4", "8.0.2").forEach { gradleVersionUnderTest ->
        targets.register("test${gradleVersionUnderTest}") {
            testTask {
                group = LifecycleBasePlugin.VERIFICATION_GROUP
                description = "Runs tests against Gradle $gradleVersionUnderTest"
                useJUnitPlatform {
                    excludeTags("no-cross-version")
                }
                systemProperty("gradleVersionUnderTest", gradleVersionUnderTest)

                exclude("**/*SamplesTest.class") // Not yet cross-version ready
            }
        }
    }
    targets.all {
        testTask {
            maxParallelForks = 4
            inputs.dir(layout.projectDirectory.dir("samples"))
            inputs.dir("src/docs/samples")
        }
    }
}

