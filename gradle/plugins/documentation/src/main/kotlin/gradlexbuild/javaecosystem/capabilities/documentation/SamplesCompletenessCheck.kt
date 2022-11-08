package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.net.URLClassLoader

abstract class SamplesCompletenessCheck : DefaultTask() {

    @get:InputFiles
    abstract val samplesBuildFiles: ConfigurableFileCollection

    @get:Classpath
    abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun check() {
        samplesBuildFiles.files.forEach { buildFile ->
            val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
            val loader = URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
            val reflectionConfiguration = ConfigurationBuilder().setUrls(ClasspathHelper.forClassLoader(loader)).addClassLoaders(loader)
            val allClasses = org.reflections.Reflections(reflectionConfiguration).getSubTypesOf(ComponentMetadataRule::class.java).filter {
                it.`package`.name == "de.jjohannes.gradle.javaecosystem.capabilities.rules" }

            if (allClasses.isEmpty()) {
                throw RuntimeException("No rule implementations found")
            }

            val missing = allClasses.map { ruleClass ->
                if (ruleClass.simpleName.startsWith("Javax")) {
                    ruleClass.getDeclaredField("FIRST_JAKARTA_VERSION")
                }
                val capabilityGroup = ruleClass.getDeclaredField("CAPABILITY_GROUP").get(null)
                val capabilityName = ruleClass.getDeclaredField("CAPABILITY_NAME").get(null)
                val capability = ruleClass.getDeclaredField("CAPABILITY").get(null)
                if (capability != "$capabilityGroup:$capabilityName") {
                    throw RuntimeException("Inconsistent fields in ${ruleClass.simpleName}: $capabilityGroup:$capabilityName | $capability")
                }
                ((ruleClass.getDeclaredField("MODULES").get(null) as Array<*>).toList() + capability).filter {
                    module -> !buildFile.readText().contains(module as String) }
            }.flatten()

            if (missing.isNotEmpty()) {
                throw RuntimeException("Missing in " + buildFile.path + ":\n" + missing.joinToString("\n") { "implementation(\"$it:+\")" })
            }
        }
    }
}