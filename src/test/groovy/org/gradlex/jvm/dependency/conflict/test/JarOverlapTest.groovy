package org.gradlex.jvm.dependency.conflict.test

import org.gradle.testfixtures.ProjectBuilder
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition
import spock.lang.Specification

import java.util.zip.ZipFile

import static org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition.*

/**
 * Test that checks that the Jar files behind each CapabilityDefinition actually have overlapping classes.
 */
class JarOverlapTest extends Specification {

    // Some Jars do not have overlapping classes, but contain conflicting implementations of the same service.
    static def expectedToOverlap = values() - [
            SLF4J_VS_JCL, // bridge vs. replacement
            SLF4J_VS_LOG4J2_FOR_JCL, // SLF4J replaces JCL, while LOG4J depends on JCL
            SLF4J_IMPL, // register conflicting service implementations
            SLF4J_VS_LOG4J2_FOR_JUL, // register conflicting handler implementations
            HAMCREST_CORE, // contains 'IsDeprecated.class' and forwards to HAMCREST
            HAMCREST_LIBRARY // contains 'IsDeprecated.class' and forwards to HAMCREST
    ]

    def latestVersions = []

    void setup() {
        latestVersions = new File("samples/sample-all/build.gradle.kts")
                .readLines()
                .findAll { it.contains("implementation(") }
                .collect { it.trim() }
                .collect { it.replace("implementation(\"", "") }
                .collect { it.replace("\")", "") }
    }

    def "capability definition is valid"(CapabilityDefinition definition) {
        given:
        def specificVersions = definitionSpecificVersions(definition)
        def project = ProjectBuilder.builder().build()
        def dependencies = project.dependencies
        project.plugins.apply("jvm-ecosystem")
        project.repositories.maven {
            url = "https://maven.scijava.org/content/groups/public"
            mavenContent { it.includeGroup("org.jzy3d") }
        }
        project.repositories.mavenCentral {
            it.metadataSources.artifact() // woodstox/wstx-lgpl/3.2.7
            it.metadataSources.ignoreGradleMetadataRedirection()
        }

        def modules = definition.modules.collect { module ->
            def specific = specificVersions.find { it.startsWith(module + ":") }
            if (specific) {
                dependencies.create(specific)
            } else {
                dependencies.create(latestVersions.find { it.startsWith(module + ":") })
            }
        }

        def conf = project.getConfigurations().detachedConfiguration(*modules)
        conf.transitive = false

        when:
        List<Tuple2<String, Set<String>>> jarClassFiles = conf.files.collect { jar ->
            def jarName = jar.name
            new Tuple2(jarName, new ZipFile(jar).withCloseable {
                it.entries().collect { entry -> entry.name }.findAll { it.endsWith(".class") } as Set
            })
        }

        then:
        [jarClassFiles, jarClassFiles].combinations().each { Tuple2<String, Set<String>> a, Tuple2<String, Set<String>> b ->
            println("Comparing: ${a.v1} <--> ${b.v1}")
            assert !a.v2.intersect(b.v2).isEmpty()
        }

        where:
        definition << expectedToOverlap
    }

    private static List<String> definitionSpecificVersions(CapabilityDefinition definition) {
        switch (definition) {
            case JAVAX_ACTIVATION_API:
                return ["jakarta.activation:jakarta.activation-api:1.2.2", "com.sun.activation:jakarta.activation:1.2.2"]
            case JAVAX_ANNOTATION_API:
                return ["jakarta.annotation:jakarta.annotation-api:1.3.5", "org.apache.tomcat:tomcat-annotations-api:9.0.104"]
            case JAVAX_EJB_API:
                return ["jakarta.ejb:jakarta.ejb-api:3.2.6"]
            case JAVAX_EL_API:
                return ["jakarta.el:jakarta.el-api:3.0.3"]
            case JAVAX_MAIL_API:
                return ["com.sun.mail:mailapi:1.6.7", "jakarta.mail:jakarta.mail-api:1.6.7", "com.sun.mail:jakarta.mail:1.6.7"]
            case JAVAX_PERSISTENCE_API:
                return ["jakarta.persistence:jakarta.persistence-api:2.2.3"]
            case JAVAX_SERVLET_API:
                return ["jakarta.servlet:jakarta.servlet-api:4.0.4", "org.apache.tomcat:servlet-api:6.0.53",
                        "org.apache.tomcat:tomcat-servlet-api:9.0.104", "org.apache.tomcat.embed:tomcat-embed-core:9.0.104"]
            case JAVAX_TRANSACTION_API:
                return ["jakarta.transaction:jakarta.transaction-api:1.3.3"]
            case JAVAX_WEBSOCKET_API_RULE:
                return ["jakarta.websocket:jakarta.websocket-api:1.1.2", "jakarta.websocket:jakarta.websocket-client-api:1.1.2",
                        "org.apache.tomcat:tomcat-websocket-api:9.0.104", "org.apache.tomcat:tomcat-websocket:9.0.104",
                        "org.apache.tomcat.embed:tomcat-embed-websocket:9.0.104"]
            case JAVAX_WS_RS_API:
                return ["jakarta.ws.rs:jakarta.ws.rs-api:2.1.6"]
            default:
                return []
        }
    }
}
