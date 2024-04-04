package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader

abstract class ReadmeUpdate : DefaultTask() {

    @get:InputFile
    abstract val readme: RegularFileProperty

    @get:Classpath
    abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun update() {
        val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
        val loader = URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
        val definitions = loader.loadClass("org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinition")

        val allCapabilities = definitions.enumConstants.map { rule ->
            val capability = definitions.getDeclaredMethod("getCapability").invoke(rule) as String
            val modules = definitions.getDeclaredMethod("getModules").invoke(rule) as List<*>

            Pair(capability, modules)
        }.sortedBy { it.first }

        val capabilityList = allCapabilities.joinToString("") { c ->
            "* ${c.first}\n${c.second.joinToString("") { "  * ${(it as String).asRepoLink()}\n" }}"
        }

        val readmeFile = readme.get().asFile
        readmeFile.writeText(
                readmeFile.readText().replace(
                        Regex("<!-- START_GENERATED -->(.*\\n)+<!-- END_GENERATED -->"),
                        "<!-- START_GENERATED -->\n$capabilityList\n<!-- END_GENERATED -->"
                )
        )
    }

    private fun String.asRepoLink() = "[$this](https://search.maven.org/artifact/${replace(":", "/")})"
}