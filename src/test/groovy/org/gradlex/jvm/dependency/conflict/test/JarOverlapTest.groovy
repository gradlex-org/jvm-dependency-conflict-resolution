package org.gradlex.jvm.dependency.conflict.test

import org.gradle.api.artifacts.Configuration
import org.gradle.testfixtures.ProjectBuilder
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.zip.ZipFile

class JarOverlapTest extends Specification {

    def allSupportedDependencies = []

    void setup() {
        allSupportedDependencies = new File("samples/sample-all/build.gradle.kts")
                .readLines()
                .findAll { it.contains("implementation(") }
                .collect { it.trim() }
                .collect { it.replace("implementation(\"", "") }
                .collect { it.replace("\")", "") }
    }

    def "it works"(CapabilityDefinition definition) {
        given:
        def project = ProjectBuilder.builder().build()
        def dependencies = project.getDependencies()
        project.getPlugins().apply("jvm-ecosystem")
        project.getRepositories().mavenCentral()

        def modules = definition.modules.collect { dependencies.create(it) }
        def constraints = allSupportedDependencies.collect { dependencies.constraints.create(it) }

        Configuration conf = project.getConfigurations().detachedConfiguration(*modules)
        conf.dependencyConstraints.addAll(constraints)
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
