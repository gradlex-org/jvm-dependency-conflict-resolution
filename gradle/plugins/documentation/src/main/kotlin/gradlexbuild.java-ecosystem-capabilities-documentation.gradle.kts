import gradlexbuild.javaecosystem.conflict.documentation.SamplesCompletenessCheck

plugins {
    id("java")
    id("gradlexbuild.java-ecosystem-capabilities-lifecycle")
    id("gradlexbuild.asciidoctor-conventions")
    id("gradlexbuild.exemplar-conventions")
}

val checkSamplesForCompleteness = tasks.register<SamplesCompletenessCheck>("checkSamplesForCompleteness") {
    pluginClasses.from(tasks.jar)
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all/build.gradle.kts"))
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all-deactivated/build.gradle.kts"))
}

tasks.quickCheck {
    dependsOn(checkSamplesForCompleteness)
}
