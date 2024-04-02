package org.gradlex.javaecosystem.capabilities.test.fixture

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

import java.lang.management.ManagementFactory
import java.nio.file.Files

class GradleBuild {

    final File projectDir
    final File settingsFile
    final File buildFile
    final File propertiesFile

    final static String GRADLE_VERSION_UNDER_TEST = System.getProperty("gradleVersionUnderTest")
    final static boolean GRADLE6_TEST = GRADLE_VERSION_UNDER_TEST?.startsWith("6.")
    final static boolean GRADLE7_TEST = GRADLE_VERSION_UNDER_TEST?.startsWith("7.")

    GradleBuild(File projectDir = Files.createTempDirectory("gradle-build").toFile()) {
        this.projectDir = projectDir
        this.settingsFile = file("settings.gradle.kts")
        this.buildFile = file("build.gradle.kts")
        this.propertiesFile = file("gradle.properties")

        settingsFile << '''
            rootProject.name = "test-project"
        '''
        buildFile << '''
        '''
        propertiesFile << '''
        '''
    }

    File file(String path) {
        new File(projectDir, path).tap {
            it.getParentFile().mkdirs()
        }
    }

    BuildResult build() {
        runner('build').build()
    }

    BuildResult fail() {
        runner('build').buildAndFail()
    }

    BuildResult printJars() {
        runner('printJars').build()
    }

    BuildResult dependenciesCompile() {
        runner('dependencies', '--configuration=compileClasspath').build()
    }

    BuildResult dependenciesRuntime() {
        runner('dependencies', '--configuration=runtimeClasspath').build()
    }

    BuildResult dependencyInsight(String module) {
        runner('dependencyInsight', '--configuration=compileClasspath', '--dependency', module).build()
    }

    GradleRunner runner(String... args) {
        GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments(Arrays.asList(args) + '-s' + '-q')
                .withDebug(ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")).with {
            GRADLE_VERSION_UNDER_TEST ? it.withGradleVersion(GRADLE_VERSION_UNDER_TEST) : it
        }
    }
}
