package org.gradlex.jvm.dependency.conflict.test

import org.gradle.api.artifacts.Configuration
import org.gradle.testfixtures.ProjectBuilder
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition
import spock.lang.Specification

import java.util.zip.ZipFile

class JarOverlapTest extends Specification {

    def "it works"(CapabilityDefinition definition) {
        given:
        def project = ProjectBuilder.builder().build()
        def dependencies = project.getDependencies()
        project.getPlugins().apply("jvm-ecosystem")
        project.getRepositories().mavenCentral()

        def modules = definition.modules.collect { dependencies.create("$it:latest.release") }
        Configuration conf = project.getConfigurations().detachedConfiguration(*modules)
        conf.transitive = false

        when:
        Map<String, Set<String>> jarClassFiles = conf.files.collectEntries { jar ->
            def jarName = jar.name
            [(jarName): new ZipFile(jar).withCloseable {
                it.entries().collect { entry -> entry.name }.findAll { it.endsWith(".class") } as Set
            }]
        }

        then:
        Collection<Set<String>> jarEntries = jarClassFiles.values()
        [jarEntries, jarEntries].combinations().each { Set a, Set b ->
            assert !a.intersect(b).isEmpty()
        }

        where:
        definition << CapabilityDefinition.values()
    }
}
