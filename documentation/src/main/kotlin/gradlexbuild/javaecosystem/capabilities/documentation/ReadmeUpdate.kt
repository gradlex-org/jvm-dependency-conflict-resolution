package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class ReadmeUpdate : DefaultTask() {

    @get:InputFile
    abstract val readme: RegularFileProperty

    @TaskAction
    fun update() {
        val reflections = org.reflections.Reflections("de.jjohannes.gradle.javaecosystem.capabilities.rules")
        val allClasses = reflections.getSubTypesOf(ComponentMetadataRule::class.java)
        val allCapabilities = allClasses.map { ruleClass ->
            val capabilityGroup = ruleClass.getDeclaredField("CAPABILITY_GROUP").get(null) as String
            var capability = "";
            ruleClass.declaredFields.filter { it.name.startsWith("CAPABILITY_NAME") }.forEach { field ->
                if (capability.isNotEmpty()) {
                    capability += " / "
                }
                val capabilityName = field.get(null) as String
                capability += "$capabilityGroup:${capabilityName}".asRepoLink()
            }
            capability += " ([${ruleClass.simpleName}](plugin/src/main/java/de/jjohannes/gradle/javaecosystem/capabilities/rules/${ruleClass.simpleName}.java))"

            val modules = ruleClass.getDeclaredField("MODULES").get(null) as Array<*>
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

    private fun String.asRepoLink() = "[$this](https://mvnrepository.com/artifact/${replace(":", "/")})"
}