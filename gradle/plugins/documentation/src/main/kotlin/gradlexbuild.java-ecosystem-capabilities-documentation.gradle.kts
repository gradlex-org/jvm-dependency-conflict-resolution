import gradlexbuild.javaecosystem.capabilities.documentation.ReadmeUpdate
import gradlexbuild.javaecosystem.capabilities.documentation.SamplesCompletenessCheck

plugins {
    id("java")
    id("gradlexbuild.java-ecosystem-capabilities-lifecycle")
}

val updateReadme = tasks.register<ReadmeUpdate>("updateReadme") {
    pluginClasses.from(tasks.jar)
    readme.set(layout.projectDirectory.file("README.MD"))
}

val checkSamplesForCompleteness = tasks.register<SamplesCompletenessCheck>("checkSamplesForCompleteness") {
    pluginClasses.from(tasks.jar)
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all/build.gradle.kts"))
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all-deactivated/build.gradle.kts"))
}

tasks.quickCheck {
    dependsOn(updateReadme)
    dependsOn(checkSamplesForCompleteness)
}
