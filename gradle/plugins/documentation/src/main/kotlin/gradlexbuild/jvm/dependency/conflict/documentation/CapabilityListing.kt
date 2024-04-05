package gradlexbuild.jvm.dependency.conflict.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader

abstract class CapabilityListing : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Classpath
    abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun update() {
        val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
        val loader = URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
        val definitions = loader.loadClass("org.gradlex.javaecosystem.conflict.detection.rules.CapabilityDefinition")

        val allCapabilities = definitions.enumConstants.map { rule ->
            val capability = definitions.getDeclaredMethod("getCapability").invoke(rule) as String
            val modules = definitions.getDeclaredMethod("getModules").invoke(rule) as List<*>

            Pair(capability, modules)
        }.sortedBy { it.first }

        val capabilityList = allCapabilities.joinToString("") { c ->
            "* ${c.first}\n${c.second.joinToString("") { "** ${(it as String).asRepoLink()}\n" }}"
        }

        outputFile.get().asFile.also {
            it.parentFile.mkdirs()
            it.writeText(capabilityList)
        }
    }

    private fun String.asRepoLink() = "https://search.maven.org/artifact/${replace(":", "/")}[$this]"
}