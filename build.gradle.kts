import java.net.URLClassLoader
import org.asciidoctor.gradle.base.AsciidoctorAttributeProvider

version = "2.4"

publishingConventions {
    pluginPortal("org.gradlex.jvm-dependency-conflict-resolution") {
        implementationClass("org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin")
        displayName("JVM Conflict Resolution Gradle Plugin")
        description("Convenient dependency conflict management for Java projects.")
        tags(
            "dependency",
            "dependencies",
            "dependency-management",
            "capabilities",
            "java",
            "logging",
            "asm",
            "bouncycastle",
            "cglib",
            "commons-io",
            "dom4j",
            "guava",
            "hamcrest",
            "javax",
            "jakarta",
            "junit",
            "mysql",
            "postgresql",
            "stax",
            "slf4j",
            "log4j2",
            "velocity",
            "woodstox",
        )
    }
    pluginPortal("org.gradlex.jvm-dependency-conflict-detection") {
        implementationClass("org.gradlex.jvm.dependency.conflict.detection.JvmDependencyConflictDetectionPlugin")
        displayName("JVM Conflict Detection Gradle Plugin")
        description("Adds Capabilities to well-known Components hosted on Maven Central.")
    }
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

testingConventions { testGradleVersions("6.8.3", "6.9.4", "7.0.2", "8.0.2", "8.14.3") }

val generateCapabilitiesList =
    tasks.register<CapabilityListing>("generateCapabilitiesList") {
        pluginClasses.from(tasks.jar)
        outputFile = layout.buildDirectory.file("generated/docs/asciidoc/parts/capabilities-listing.adoc")
    }

tasks.asciidoctor {
    dependsOn(generateCapabilitiesList)
    attributeProviders += AsciidoctorAttributeProvider {
        mapOf("capabilities-listing" to generateCapabilitiesList.get().outputFile.get().asFile.absolutePath)
    }
}

abstract class CapabilityListing : DefaultTask() {

    @get:OutputFile abstract val outputFile: RegularFileProperty

    @get:Classpath abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun update() {
        val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
        val loader =
            URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
        val definitions = loader.loadClass("org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition")

        val allCapabilities =
            definitions.enumConstants
                .map { rule ->
                    val capability = definitions.getDeclaredMethod("getCapability").invoke(rule) as String
                    val modules = definitions.getDeclaredMethod("getModules").invoke(rule) as List<*>

                    Pair(capability, modules)
                }
                .sortedBy { it.first }

        val capabilityList =
            allCapabilities.joinToString("") { c ->
                "* ${c.first}\n${c.second.joinToString("") { "** ${(it as String).asRepoLink()}\n" }}"
            }

        outputFile.get().asFile.also {
            it.parentFile.mkdirs()
            it.writeText(capabilityList)
        }
    }

    private fun String.asRepoLink() = "https://search.maven.org/artifact/${replace(":", "/")}[$this]"
}

// === the following custom configuration should be removed once tests are migrated to Java
apply(plugin = "groovy")

tasks.named<GroovyCompile>("compileTestGroovy") { targetCompatibility = "11" } // allow tests to run against 6.x

dependencies { testImplementation("org.spockframework:spock-core:2.3-groovy-4.0") } //
// ====================================================================================
