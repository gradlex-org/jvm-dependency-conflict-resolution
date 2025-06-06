// Workaround for the fact that test suites are always added to check.
// Since we set up test suites for long running cross version tests
// we want to have a lifecycle task that runs all quality checks and
// the default tests.
tasks.register("quickCheck") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs the default test task as well as code verifications such as Checkstyle"
    dependsOn(tasks.named("assemble"))
    dependsOn(tasks.named("test"))
    dependsOn(tasks.named("checkstyleMain"))
    dependsOn(tasks.named("checkstyleTest"))
}

