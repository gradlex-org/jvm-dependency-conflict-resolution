package gradlexbuild.javaecosystem.capabilities.documentation

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.net.URLClassLoader
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

abstract class ReadmeUpdate : DefaultTask() {

    @get:InputFile
    abstract val readme: RegularFileProperty

    @get:Classpath
    abstract val pluginClasses: ConfigurableFileCollection

    @TaskAction
    fun update() {
        val classesUrls = pluginClasses.files.map { it.toURI().toURL() }
        val loader = URLClassLoader("pluginClasspath", classesUrls.toTypedArray(), ComponentMetadataRule::class.java.classLoader)
        val reflectionConfiguration = ConfigurationBuilder().setUrls(ClasspathHelper.forClassLoader(loader)).addClassLoaders(loader)
        val allClasses = org.reflections.Reflections(reflectionConfiguration).getSubTypesOf(ComponentMetadataRule::class.java).filter {
            it.`package`.name == "de.jjohannes.gradle.javaecosystem.capabilities.rules" }

        if (allClasses.isEmpty()) {
            throw RuntimeException("No rule implementations found")
        }

        val allCapabilities = allClasses.map { ruleClass ->
            val capabilityGroup = ruleClass.getDeclaredField("CAPABILITY_GROUP").get(null) as String
            var capability = ""
            ruleClass.declaredFields.filter { it.name.startsWith("CAPABILITY_NAME") }.forEach { field ->
                if (capability.isNotEmpty()) {
                    capability += " / "
                }
                val capabilityName = field.get(null) as String
                capability += "$capabilityGroup:${capabilityName}".asRepoLink()
            }
            capability += " ([${ruleClass.simpleName}](src/main/java/de/jjohannes/gradle/javaecosystem/capabilities/rules/${ruleClass.simpleName}.java))"

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