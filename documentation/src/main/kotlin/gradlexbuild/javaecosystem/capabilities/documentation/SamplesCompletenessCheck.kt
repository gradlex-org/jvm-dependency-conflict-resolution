package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

abstract class SamplesCompletenessCheck : DefaultTask() {

    @get:InputFiles
    abstract val samplesBuildFiles: ConfigurableFileCollection

    @TaskAction
    fun check() {
        samplesBuildFiles.files.forEach { buildFile ->
            val reflections = org.reflections.Reflections("de.jjohannes.gradle.javaecosystem.capabilities.rules")
            val allClasses = reflections.getSubTypesOf(ComponentMetadataRule::class.java)

            val missing = allClasses.map { ruleClass ->
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