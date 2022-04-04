tasks.register("check") {
    dependsOn(gradle.includedBuild("documentation").task(":checkAndUpdate"))
    dependsOn(gradle.includedBuild("plugin").task(":check"))
}
