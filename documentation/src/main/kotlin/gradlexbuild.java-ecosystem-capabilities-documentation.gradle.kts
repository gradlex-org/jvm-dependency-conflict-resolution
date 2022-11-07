import gradlexbuild.javaecosystem.capabilities.documentation.ReadmeUpdate
import gradlexbuild.javaecosystem.capabilities.documentation.SamplesCompletenessCheck

plugins {
    id("base")
}

val updateReadme = tasks.register<ReadmeUpdate>("updateReadme") {
    readme.set(layout.projectDirectory.file("README.MD"))
}

val checkAllSample = tasks.register<SamplesCompletenessCheck>("checkSamplesForCompleteness") {
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all/build.gradle.kts"))
    samplesBuildFiles.from(layout.projectDirectory.file("samples/sample-all-deactivated/build.gradle.kts"))
}

tasks.check {
    dependsOn(updateReadme)
    dependsOn(checkAllSample)
}
