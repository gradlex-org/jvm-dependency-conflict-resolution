plugins {
    id("de.jjohannes.gradle.java-ecosystem-capabilities")
}

buildscript {
    dependencies {
        classpath("org.reflections:reflections:0.10.2")
    }
}

fun String.asRepoLink() = "[$this](https://mvnrepository.com/artifact/${replace(":", "/")})"

val updateReadme = tasks.register("updateReadme") {
    doLast {
        val readme = layout.projectDirectory.file("../README.MD").asFile

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

        readme.writeText(
            readme.readText().replace(
                Regex("<!-- START_GENERATED -->(.*\\n)+<!-- END_GENERATED -->"),
                "<!-- START_GENERATED -->\n$capabilityList\n<!-- END_GENERATED -->"
            )
        )
    }
}

val checkAllSample = tasks.register("checkAllSample") {
    doLast {
        val buildFile = layout.projectDirectory.file("../samples/sample-all/build.gradle.kts").asFile.readText()

        val reflections = org.reflections.Reflections("de.jjohannes.gradle.javaecosystem.capabilities.rules")
        val allClasses = reflections.getSubTypesOf(ComponentMetadataRule::class.java)

        val missing = allClasses.map { ruleClass ->
            val capabilityGroup = ruleClass.getDeclaredField("CAPABILITY_GROUP").get(null)
            val capabilityName = ruleClass.declaredFields.find { it.name.startsWith("CAPABILITY_NAME") }!!.get(null)
            ((ruleClass.getDeclaredField("MODULES").get(null) as Array<*>).toList() + "$capabilityGroup:$capabilityName").filter {
                    module -> !buildFile.contains(module as String) }
        }.flatten()

        if (missing.isNotEmpty()) {
            throw RuntimeException(missing.joinToString("\n") { "implementation(\"$it:+\")" })
        }
    }
}

tasks.register("checkAndUpdate") {
    dependsOn(updateReadme)
    dependsOn(checkAllSample)
}
