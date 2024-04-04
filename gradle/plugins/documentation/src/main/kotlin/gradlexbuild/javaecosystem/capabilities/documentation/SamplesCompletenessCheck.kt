package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.net.URLClassLoader

abstract class SamplesCompletenessCheck : DefaultTask() {

    @get:InputFiles
    abstract val samplesBuildFiles: ConfigurableFileCollection

    @get:Classpath
    abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun check() {
        samplesBuildFiles.files.forEach { buildFile ->
            val testBuildFileContent = buildFile.readText()

            val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
            val loader = URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
            val definitions = loader.loadClass("org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinition")

            val missing = definitions.enumConstants.map { rule ->
                val modules = definitions.getDeclaredMethod("getModules").invoke(rule) as List<*>
                modules.filter { module -> !testBuildFileContent.contains(module as String) }
            }.flatten().distinct()

            if (missing.isNotEmpty()) {
                throw RuntimeException("Missing in " + buildFile.path + ":\n" + missing.joinToString("\n") { "implementation(\"$it:+\")" })
            }
        }
    }
}