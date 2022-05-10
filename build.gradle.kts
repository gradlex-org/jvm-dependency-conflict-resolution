tasks.register("check") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    dependsOn(gradle.includedBuild("documentation").task(":checkAndUpdate"))
    dependsOn(gradle.includedBuild("plugin").task(":check"))
}
