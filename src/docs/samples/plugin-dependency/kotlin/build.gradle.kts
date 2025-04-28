plugins {
    id("java")
}

repositories.gradlePluginPortal()

// tag::plugin-dependency[]
dependencies {
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.3")
}
// end::plugin-dependency[]