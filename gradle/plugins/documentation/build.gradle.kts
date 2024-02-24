plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":build-lifecycle"))
    implementation("org.reflections:reflections:0.10.2")
}
