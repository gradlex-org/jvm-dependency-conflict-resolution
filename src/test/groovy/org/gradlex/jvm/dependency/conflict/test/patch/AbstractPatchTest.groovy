package org.gradlex.jvm.dependency.conflict.test.patch

import org.gradlex.jvm.dependency.conflict.test.fixture.GradleBuild
import spock.lang.Specification

abstract class AbstractPatchTest extends Specification {

    @Delegate
    GradleBuild build = new GradleBuild()

    def setup() {
        file('src/main/java/Dummy.java') << 'class Dummy {}'
        buildFile << """
            plugins {
                id("org.gradlex.jvm-dependency-conflict-resolution")
                id("java-library")
            }
            repositories.mavenCentral()
        """
    }
}
