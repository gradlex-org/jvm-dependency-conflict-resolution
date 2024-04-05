import gradlexbuild.jvm.dependency.conflict.documentation.CapabilityListing
import org.asciidoctor.gradle.base.AsciidoctorAttributeProvider
import org.asciidoctor.gradle.base.log.Severity

plugins {
    id("java")
    id("org.asciidoctor.jvm.convert")
}

tasks {
    val generateCapabilitiesList by tasks.registering(CapabilityListing::class) {
        pluginClasses.from(tasks.jar)
        outputFile = layout.buildDirectory.file("generated/docs/asciidoc/parts/capabilities-listing.adoc")
    }

    asciidoctor {
        notCompatibleWithConfigurationCache("See https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/564")

        failureLevel = Severity.WARN

        attributes(mapOf(
            "docinfodir" to "src/docs/asciidoc",
            "docinfo" to "shared",
            "imagesdir" to "./images",
            "source-highlighter" to "prettify",
            "tabsize" to "4",
            "toc" to "left",
            "tip-caption" to "💡",
            "note-caption" to "ℹ️",
            "important-caption" to "❗",
            "caution-caption" to "🔥",
            "warning-caption" to "⚠️",
            "sectanchors" to true,
            "idprefix" to "",
            "idseparator" to "-",
            "samples-path" to "$projectDir/src/docs/samples"
        ))

        attributeProviders += AsciidoctorAttributeProvider {
            mapOf("capabilities-listing" to generateCapabilitiesList.get().outputFile.get().asFile.absolutePath)
        }
        dependsOn(generateCapabilitiesList)

        inputs.dir("src/docs/samples")
                .withPathSensitivity(PathSensitivity.RELATIVE)
                .withPropertyName("samples")
    }
}
