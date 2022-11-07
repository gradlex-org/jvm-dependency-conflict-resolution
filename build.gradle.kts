plugins {
    id("gradlexbuild.java-ecosystem-capabilities-documentation")
}

tasks.check {
    dependsOn(gradle.includedBuild("plugin").task(":checkAllVersions"))
}
