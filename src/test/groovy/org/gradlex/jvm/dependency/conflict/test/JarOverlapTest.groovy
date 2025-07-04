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
    private static final String SAMPLE_ALL_BUILD_FILE = "samples/sample-all/build.gradle.kts"
    private static final String SAMPLE_ALL_DEACTIVATED_BUILD_FILE = "samples/sample-all-deactivated/build.gradle.kts"

    // Some Jars do not have overlapping classes, but contain conflicting implementations of the same service.
    static def expectedToOverlap = values() - [
            HAMCREST_CORE, // contains 'IsDeprecated.class' and forwards to HAMCREST
            HAMCREST_LIBRARY, // contains 'IsDeprecated.class' and forwards to HAMCREST
            LOG4J2_IMPL,
            SLF4J_IMPL, // register conflicting service implementations
            SLF4J_VS_JCL, // bridge vs. replacement
            SLF4J_VS_JUL,
            SLF4J_VS_LOG4J2_FOR_JCL, // SLF4J replaces JCL, while LOG4J depends on JCL
            SLF4J_VS_LOG4J2_FOR_JUL // register conflicting handler implementations
    ]

    def allSample = parse(SAMPLE_ALL_BUILD_FILE)
    def allDeactivatedSample = parse(SAMPLE_ALL_DEACTIVATED_BUILD_FILE)

    private static List<String> parse(String buildFilr) {
        new File(buildFilr)
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
            url = "https://maven.jzy3d.org/releases"
            mavenContent {
                it.includeModule("org.jzy3d", "jGL")
                it.includeModule("org.jzy3d", "jzy3d-jGL")
                it.includeModule("org.jzy3d", "jzy3d-jGL-awt")
            }
        }
        project.repositories.maven {
            url = "https://maven.scijava.org/content/groups/public"
            mavenContent {
                it.includeGroup("org.jzy3d")
            }
        }
        project.repositories.mavenCentral {
            it.metadataSources.artifact() // woodstox/wstx-lgpl/3.2.7
            it.metadataSources.ignoreGradleMetadataRedirection()
        }

        def missingInAll = []
        def missingInAllDeactivated = []

        def modules = definition.modules.collect { module ->
            def specific = specificVersions.find { it.startsWith(module + ":") }
            def moduleInAllSample = allSample.find { it.startsWith(module + ":") }
            def moduleInAllDeactivatedSample = allDeactivatedSample.find { it.startsWith(module + ":") }
            if (!moduleInAllSample) {
                missingInAll.add(module)
            }
            if (!moduleInAllDeactivatedSample) {
                missingInAllDeactivated.add(module)
            }

            if (specific) {
                dependencies.create(specific)
            } else if (moduleInAllSample !=null ) {
                dependencies.create(moduleInAllSample)
            } else {
                null
            }
        }

        if (!missingInAll.isEmpty() || !missingInAllDeactivated.isEmpty()) {
            throw new RuntimeException(
                    "Missing in $SAMPLE_ALL_BUILD_FILE:\n" + missingInAll.collect { "implementation(\"$it:+\")\n" }.join("") +
                    "Missing in $SAMPLE_ALL_DEACTIVATED_BUILD_FILE:\n" + missingInAllDeactivated.collect { "implementation(\"$it:+\")\n" }.join("")
            )
        }

        def conf = project.getConfigurations().detachedConfiguration(*modules)
        conf.transitive = false

        when:
        List<Tuple2<String, Set<String>>> jarClassFiles = conf.files.collect { jar ->
            def jarName = jar.name
            new Tuple2(jarName, new ZipFile(jar).withCloseable {
                it.entries().collect { entry -> entry.name }.findAll { it.endsWith(".class") && !it.endsWith("module-info.class") } as Set
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
            case JAVAX_INJECT_API:
                return ["jakarta.inject:jakarta.inject-api:1.0.5"]
            case JAVAX_JSON_API:
                return ["jakarta.json:jakarta.json-api:1.1.6", "org.glassfish:jakarta.json:1.1.6"]
            case JAVAX_JWS_API:
                return ["jakarta.jws:jakarta.jws-api:1.1.1"]
            case JAVAX_MAIL_API:
                return ["com.sun.mail:mailapi:1.6.7", "jakarta.mail:jakarta.mail-api:1.6.7", "com.sun.mail:jakarta.mail:1.6.7"]
            case JAVAX_PERSISTENCE_API:
                return ["jakarta.persistence:jakarta.persistence-api:2.2.3"]
            case JAVAX_SERVLET_API:
                return ["jakarta.servlet:jakarta.servlet-api:4.0.4", "org.apache.tomcat:servlet-api:6.0.53",
                        "org.apache.tomcat:tomcat-servlet-api:9.0.104", "org.apache.tomcat.embed:tomcat-embed-core:9.0.104"]
            case JAVAX_SERVLET_JSP:
                return ["jakarta.servlet.jsp:jakarta.servlet.jsp-api:2.3.6"]
            case JAVAX_SERVLET_JSTL:
                return ["jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:1.2.7"]
            case JAVAX_SOAP_API:
                return ["jakarta.xml.soap:jakarta.xml.soap-api:1.4.2"]
            case JAVAX_TRANSACTION_API:
                return ["jakarta.transaction:jakarta.transaction-api:1.3.3"]
            case JAVAX_VALIDATION_API:
                return ["jakarta.validation:jakarta.validation-api:2.0.2"]
            case JAVAX_WEBSOCKET_API_RULE:
                return ["jakarta.websocket:jakarta.websocket-api:1.1.2", "jakarta.websocket:jakarta.websocket-client-api:1.1.2",
                        "org.apache.tomcat:tomcat-websocket-api:9.0.104", "org.apache.tomcat:tomcat-websocket:9.0.104",
                        "org.apache.tomcat.embed:tomcat-embed-websocket:9.0.104"]
            case JAVAX_WS_RS_API:
                return ["jakarta.ws.rs:jakarta.ws.rs-api:2.1.6"]
            case JAVAX_XML_BIND_API:
                return ["jakarta.xml.bind:jakarta.xml.bind-api:2.3.3"]
            case JAVAX_XML_WS_API:
                return ["jakarta.xml.ws:jakarta.xml.ws-api:2.3.3"]
            default:
                return []
        }
    }
}
