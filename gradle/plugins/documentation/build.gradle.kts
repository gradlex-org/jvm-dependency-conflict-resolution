plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":build-lifecycle"))
    implementation("org.asciidoctor:asciidoctor-gradle-jvm:4.0.2")
}
