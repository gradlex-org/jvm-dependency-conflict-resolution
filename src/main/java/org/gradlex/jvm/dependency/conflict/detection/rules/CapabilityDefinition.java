/*
 * Copyright the GradleX team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlex.jvm.dependency.conflict.detection.rules;

import org.gradlex.jvm.dependency.conflict.detection.rules.aopalliance.AopallianceRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.guava.GuavaListenableFutureRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaActivationApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaActivationImplementationRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaAnnotationApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaMailApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaServletApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaWebsocketApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaWebsocketClientApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JakartaWsRsApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxActivationApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxAnnotationApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxEjbApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxElApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxInjectApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxJsonApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxJwsApisRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxMailApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxPersistenceApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxServletApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxServletJspRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxServletJstlRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxSoapApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxTransactionApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxValidationApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxWebsocketApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxWsRsApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxXmlBindApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jakarta.JavaxXmlWsApiRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.LoggingModuleIdentifiers;
import org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy;

import java.util.Arrays;
import java.util.List;

import static org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy.FIRST_MODULE;
import static org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy.HIGHEST_VERSION;
import static org.gradlex.jvm.dependency.conflict.resolution.DefaultResolutionStrategy.NONE;

public enum CapabilityDefinition {

    AOPALLIANCE(HIGHEST_VERSION, AopallianceRule.class,
            "aopalliance:aopalliance",
            "org.springframework:spring-aop"
    ),
    APACHE_CSV(HIGHEST_VERSION,
            "org.apache.solr:solr-commons-csv",
            "org.apache.commons:commons-csv"
    ),
    ASM(HIGHEST_VERSION,
            "asm:asm",
            "org.ow2.asm:asm"
    ),
    BOUNCYCASTLE_BCMAIL(HIGHEST_VERSION,
            "org.bouncycastle:bcmail",
            "org.bouncycastle:bcmail-fips",
            "org.bouncycastle:bcmail-jdk14",
            "org.bouncycastle:bcmail-jdk15",
            "org.bouncycastle:bcmail-jdk15+",
            "org.bouncycastle:bcmail-jdk15on",
            "org.bouncycastle:bcmail-jdk15to18",
            "org.bouncycastle:bcmail-jdk16",
            "org.bouncycastle:bcmail-jdk18on",
            "org.bouncycastle:bcjmail-jdk15on",
            "org.bouncycastle:bcjmail-jdk15to18",
            "org.bouncycastle:bcjmail-jdk18on"
    ),
    BOUNCYCASTLE_BCPG(HIGHEST_VERSION,
            "org.bouncycastle:bcpg",
            "org.bouncycastle:bcpg-fips",
            "org.bouncycastle:bcpg-jdk12",
            "org.bouncycastle:bcpg-jdk14",
            "org.bouncycastle:bcpg-jdk15",
            "org.bouncycastle:bcpg-jdk15+",
            "org.bouncycastle:bcpg-jdk15on",
            "org.bouncycastle:bcpg-jdk15to18",
            "org.bouncycastle:bcpg-jdk16",
            "org.bouncycastle:bcpg-jdk18on"
    ),
    BOUNCYCASTLE_BCPKIX(HIGHEST_VERSION,
            "org.bouncycastle:bcpkix",
            "org.bouncycastle:bcpkix-fips",
            "org.bouncycastle:bcpkix-jdk14",
            "org.bouncycastle:bcpkix-jdk15on",
            "org.bouncycastle:bcpkix-jdk15to18",
            "org.bouncycastle:bcpkix-jdk18on"
    ),
    BOUNCYCASTLE_BCPROV(HIGHEST_VERSION,
            "org.bouncycastle:bcprov",
            "org.bouncycastle:bcprov-debug-jdk14",
            "org.bouncycastle:bcprov-debug-jdk15on",
            "org.bouncycastle:bcprov-debug-jdk15to18",
            "org.bouncycastle:bcprov-debug-jdk18on",
            "org.bouncycastle:bcprov-ext-debug-jdk14",
            "org.bouncycastle:bcprov-ext-debug-jdk15on",
            "org.bouncycastle:bcprov-ext-debug-jdk15to18",
            "org.bouncycastle:bcprov-ext-debug-jdk18on",
            "org.bouncycastle:bcprov-ext-jdk14",
            "org.bouncycastle:bcprov-ext-jdk15",
            "org.bouncycastle:bcprov-ext-jdk15on",
            "org.bouncycastle:bcprov-ext-jdk15to18",
            "org.bouncycastle:bcprov-ext-jdk16",
            "org.bouncycastle:bcprov-ext-jdk18on",
            "org.bouncycastle:bcprov-jdk12",
            "org.bouncycastle:bcprov-jdk14",
            "org.bouncycastle:bcprov-jdk15",
            "org.bouncycastle:bcprov-jdk15+",
            "org.bouncycastle:bcprov-jdk15on",
            "org.bouncycastle:bcprov-jdk15to18",
            "org.bouncycastle:bcprov-jdk16",
            "org.bouncycastle:bcprov-jdk18on",
            "org.bouncycastle:bc-fips",
            "org.bouncycastle:bc-fips-debug"
    ),
    BOUNCYCASTLE_BCTLS(HIGHEST_VERSION,
            "org.bouncycastle:bctls",
            "org.bouncycastle:bctls-jdk14",
            "org.bouncycastle:bctls-jdk15on",
            "org.bouncycastle:bctls-jdk15to18",
            "org.bouncycastle:bctls-jdk18on",
            "org.bouncycastle:bctls-fips"
    ),
    BOUNCYCASTLE_BCTSP(HIGHEST_VERSION,
            "org.bouncycastle:bctsp",
            "org.bouncycastle:bctsp-jdk14",
            "org.bouncycastle:bctsp-jdk15",
            "org.bouncycastle:bctsp-jdk15+",
            "org.bouncycastle:bctsp-jdk15on",
            "org.bouncycastle:bctsp-jdk16"
    ),
    BOUNCYCASTLE_BCUTIL(HIGHEST_VERSION,
            "org.bouncycastle:bcutil",
            "org.bouncycastle:bcutil-jdk14",
            "org.bouncycastle:bcutil-jdk15on",
            "org.bouncycastle:bcutil-jdk15to18",
            "org.bouncycastle:bcutil-jdk18on"
    ),
    C3P0(HIGHEST_VERSION,
            "c3p0:c3p0",
            "com.mchange:c3p0"
    ),
    MCHANGE_COMMONS_JAVA(HIGHEST_VERSION,
            "c3p0:c3p0",
            "com.mchange:mchange-commons-java"
    ),
    CGLIB(HIGHEST_VERSION,
            "cglib:cglib",
            "cglib:cglib-nodep"
    ),
    COMMONS_BEANUTILS(HIGHEST_VERSION,
            "commons-beanutils:commons-beanutils-core",
            "commons-beanutils:commons-beanutils"
    ),
    COMMONS_IO(HIGHEST_VERSION,
            "commons-io:commons-io",
            "org.apache.commons:commons-io"
    ),
    DOM4J(HIGHEST_VERSION,
            "dom4j:dom4j",
            "org.dom4j:dom4j"
    ),
    FINDBUGS_ANNOTATIONS(HIGHEST_VERSION,
            "com.google.code.findbugs:annotations",
            "com.google.code.findbugs:findbugs-annotations",
            "com.github.spotbugs:spotbugs-annotations"
    ),
    GOOGLE_COLLECTIONS(HIGHEST_VERSION, "com.google.collections", CapabilityDefinitionRule.class,
            "com.google.collections:google-collections",
            "com.google.guava:guava"
    ),
    GUAVA(HIGHEST_VERSION,
            "com.google.guava:guava",
            "com.google.guava:guava-jdk5"
    ),
    JZY3D_EMUL_GL(HIGHEST_VERSION,
            "org.jzy3d:jzy3d-emul-gl",
            "org.jzy3d:jzy3d-emul-gl-awt"
    ),
    JZY3D_JGL(HIGHEST_VERSION,
            "org.jzy3d:jGL",
            "org.jzy3d:jzy3d-jGL-awt"
    ),
    LZ4(HIGHEST_VERSION,
            "net.jpountz.lz4:lz4",
            "org.lz4:lz4-java"
    ),
    LISTENABLEFUTURE(FIRST_MODULE, "com.google.guava", GuavaListenableFutureRule.class,
            "com.google.guava:guava",
            "com.google.guava:listenablefuture"
    ),
    HAMCREST_LIBRARY(FIRST_MODULE,
            "org.hamcrest:hamcrest",
            "org.hamcrest:hamcrest-library"
    ),
    HAMCREST_CORE(FIRST_MODULE,
            "org.hamcrest:hamcrest",
            "org.hamcrest:hamcrest-core"
    ),
    HIKARI_CP(HIGHEST_VERSION,
            "com.zaxxer:HikariCP",
            "com.zaxxer:HikariCP-java6",
            "com.zaxxer:HikariCP-java7"
    ),
    INTELLIJ_ANNOTATIONS(HIGHEST_VERSION,
            "org.jetbrains:annotations",
            "com.intellij:annotations"
    ),
    JAVA_ASSIST(HIGHEST_VERSION,
            "javassist:javassist",
            "org.javassist:javassist",
            "jboss:javassist"
    ),
    JCIP_ANNOTATIONS(HIGHEST_VERSION,
            "net.jcip:jcip-annotations",
            "com.github.stephenc.jcip:jcip-annotations"
    ),
    JNA_PLATFORM(HIGHEST_VERSION,
            "net.java.dev.jna:platform",
            "net.java.dev.jna:jna-platform"
    ),
    JTS_CORE(HIGHEST_VERSION,
            "com.vividsolutions:jts",
            "com.vividsolutions:jts-core"
    ),
    JUNIT(HIGHEST_VERSION,
            "junit:junit",
            "junit:junit-dep"
    ),
    MIGLAYOUT(HIGHEST_VERSION,
            "com.miglayout:miglayout-swing",
            "com.miglayout:miglayout"
    ),
    /**
     * See <a href="https://github.com/spring-projects/spring-boot/issues/32881">spring-projects/spring-boot/issues/32881</a>
     */
    ORG_JSON(HIGHEST_VERSION,
            "com.vaadin.external.google:android-json",
            "org.json:json"
    ),
    MYSQL_CONNECTOR_JAVA(HIGHEST_VERSION,
            "mysql:mysql-connector-java",
            "com.mysql:mysql-connector-j"
    ),
    POSTGRESQL(HIGHEST_VERSION,
            "postgresql:postgresql",
            "org.postgresql:postgresql"
    ),
    STAX_API(HIGHEST_VERSION,
            "stax:stax-api",
            "javax.xml.stream:stax-api"
    ),
    VELOCITY(HIGHEST_VERSION,
            "velocity:velocity",
            "org.apache.velocity:velocity",
            "org.apache.velocity:velocity-engine-core"
    ),
    WOODSTOX_ASL(HIGHEST_VERSION,
            "org.codehaus.woodstox:woodstox-core-asl",
            "org.codehaus.woodstox:woodstox-core-lgpl",
            "org.codehaus.woodstox:wstx-asl",
            "org.codehaus.woodstox:wstx-lgpl",
            "woodstox:wstx-asl"
    ),
    JAKARTA_ACTIVATION_API(HIGHEST_VERSION, JakartaActivationApiRule.class,
            "jakarta.activation:jakarta.activation-api",
            "com.sun.activation:jakarta.activation"
    ),
    JAKARTA_ACTIVATION_IMPL(HIGHEST_VERSION, JakartaActivationImplementationRule.class,
            "com.sun.activation:jakarta.activation",
            "org.eclipse.angus:angus-activation"
    ),
    JAKARTA_ANNOTATION_API(HIGHEST_VERSION, JakartaAnnotationApiRule.class,
            "jakarta.annotation:jakarta.annotation-api",
            "org.apache.tomcat:tomcat-annotations-api"
    ),
    JAKARTA_JSON_API(HIGHEST_VERSION,
            "jakarta.json:jakarta.json-api",
            "org.glassfish:jakarta.json"
    ),
    JAKARTA_MAIL_API(HIGHEST_VERSION, JakartaMailApiRule.class,
            "jakarta.mail:jakarta.mail-api",
            "com.sun.mail:mailapi",
            "com.sun.mail:jakarta.mail",
            "org.eclipse.angus:jakarta.mail"
    ),
    JAKARTA_SERVLET_API(HIGHEST_VERSION, JakartaServletApiRule.class,
            "jakarta.servlet:jakarta.servlet-api",
            "org.apache.tomcat:tomcat-servlet-api",
            "org.apache.tomcat.embed:tomcat-embed-core",
            "org.eclipse.jetty.toolchain:jetty-jakarta-servlet-api"
    ),
    JAKARTA_WEBSOCKET_API(HIGHEST_VERSION, JakartaWebsocketApiRule.class,
            "jakarta.websocket:jakarta.websocket-api",
            "org.apache.tomcat:tomcat-websocket-api",
            "org.apache.tomcat:tomcat-websocket",
            "org.apache.tomcat.embed:tomcat-embed-websocket",
            "org.eclipse.jetty.toolchain:jetty-jakarta-websocket-api"
    ),
    JAKARTA_WEBSOCKET_CLIENT_API(HIGHEST_VERSION, JakartaWebsocketClientApiRule.class,
            "jakarta.websocket:jakarta.websocket-client-api",
            "org.apache.tomcat:tomcat-websocket-client-api",
            "org.apache.tomcat:tomcat-websocket",
            "org.apache.tomcat.embed:tomcat-embed-websocket",
            "org.eclipse.jetty.toolchain:jetty-jakarta-websocket-api"
    ),
    JAKARTA_WS_RS_API(HIGHEST_VERSION, JakartaWsRsApiRule.class,
            "jakarta.ws.rs:jakarta.ws.rs-api",
            "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_3.0_spec"
    ),

    JAVAX_ACTIVATION_API(HIGHEST_VERSION, JavaxActivationApiRule.class,
            "javax.activation:activation",
            "jakarta.activation:jakarta.activation-api",
            "javax.activation:javax.activation-api",
            "com.sun.activation:javax.activation",
            "com.sun.activation:jakarta.activation"
    ),
    JAVAX_ANNOTATION_API(HIGHEST_VERSION, JavaxAnnotationApiRule.class,
            "javax.annotation:jsr250-api",
            "jakarta.annotation:jakarta.annotation-api",
            "javax.annotation:javax.annotation-api",
            "org.apache.tomcat:tomcat-annotations-api"
    ),
    JAVAX_EJB_API(HIGHEST_VERSION, JavaxEjbApiRule.class,
            "javax.ejb:ejb",
            "jakarta.ejb:jakarta.ejb-api",
            "javax.ejb:javax.ejb-api",
            "javax.ejb:ejb-api"
    ),
    JAVAX_EL_API(HIGHEST_VERSION, JavaxElApiRule.class,
            "javax.el:el-api",
            "jakarta.el:jakarta.el-api",
            "javax.el:javax.el-api"
    ),
    JAVAX_INJECT_API(HIGHEST_VERSION, JavaxInjectApiRule.class,
            "javax.inject:javax.inject",
            "jakarta.inject:jakarta.inject-api",
            "com.jwebmp:javax.inject",
            "org.glassfish.hk2.external:javax.inject",
            "org.glassfish.hk2.external:jakarta.inject"
    ),
    JAVAX_JSON_API(HIGHEST_VERSION, JavaxJsonApiRule.class,
            "javax.json:javax.json-api",
            "jakarta.json:jakarta.json-api",
            "org.glassfish:javax.json",
            "org.glassfish:jakarta.json"
    ),
    JAVAX_JWS_API(HIGHEST_VERSION, JavaxJwsApisRule.class,
            "javax.jws:javax.jws-api",
            "jakarta.jws:jakarta.jws-api"
    ),
    JAVAX_MAIL_API(HIGHEST_VERSION, JavaxMailApiRule.class,
            "com.sun.mail:mailapi", // API only
            "jakarta.mail:jakarta.mail-api", // API only
            "javax.mail:javax.mail-api", // API only
            "javax.mail:mail", // API + Implementation
            "com.sun.mail:javax.mail", // API + Implementation
            "com.sun.mail:jakarta.mail", // API + Implementation
            "org.apache.geronimo.javamail:geronimo-javamail_1.3.1_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.3.1_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.3.1_spec",
            "org.apache.geronimo.javamail:geronimo-javamail_1.4_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.4_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.4_spec",
            "org.apache.geronimo.javamail:geronimo-javamail_1.6_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.6_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.6_spec"
    ),
    JAVAX_PERSISTENCE_API(HIGHEST_VERSION, JavaxPersistenceApiRule.class,
            "javax.persistence:javax.persistence-api",
            "jakarta.persistence:jakarta.persistence-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.2-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.1-api",
            "org.hibernate.javax.persistence:hibernate-jpa-2.0-api"
    ),
    JAVAX_SERVLET_API(HIGHEST_VERSION, JavaxServletApiRule.class,
            "javax.servlet:servlet-api",
            "javax.servlet:javax.servlet-api",
            "jakarta.servlet:jakarta.servlet-api",
            "org.apache.tomcat:servlet-api",
            "org.apache.tomcat:tomcat-servlet-api",
            "org.apache.tomcat.embed:tomcat-embed-core",
            "servletapi:servletapi"
    ),
    JAVAX_SERVLET_JSP(HIGHEST_VERSION, JavaxServletJspRule.class,
            "javax.servlet:jsp-api",
            "javax.servlet.jsp:jsp-api",
            "javax.servlet.jsp:javax.servlet.jsp-api",
            "jakarta.servlet.jsp:jakarta.servlet.jsp-api"
    ),
    JAVAX_SERVLET_JSTL(HIGHEST_VERSION, JavaxServletJstlRule.class,
            "javax.servlet:jstl",
            "javax.servlet.jsp.jstl:jstl-api",
            "javax.servlet.jsp.jstl:javax.servlet.jsp.jstl-api",
            "jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api"
    ),
    JAVAX_SOAP_API(HIGHEST_VERSION, JavaxSoapApiRule.class,
            "javax.xml.soap:javax.xml.soap-api",
            "jakarta.xml.soap:jakarta.xml.soap-api"
    ),
    JAVAX_TRANSACTION_API(HIGHEST_VERSION, JavaxTransactionApiRule.class,
            "javax.transaction:jta",
            "javax.transaction:javax.transaction-api",
            "jakarta.transaction:jakarta.transaction-api",
            "org.jboss.spec.javax.transaction:jboss-transaction-api_1.1_spec",
            "org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec",
            "org.jboss.spec.javax.transaction:jboss-transaction-api_1.3_spec"
    ),
    JAVAX_VALIDATION_API(HIGHEST_VERSION, JavaxValidationApiRule.class,
            "javax.validation:validation-api",
            "jakarta.validation:jakarta.validation-api"
    ),
    JAVAX_WEBSOCKET_API_RULE(HIGHEST_VERSION, JavaxWebsocketApiRule.class,
            "javax.websocket:javax.websocket-api",
            "jakarta.websocket:jakarta.websocket-api",
            "javax.websocket:javax.websocket-client-api", // in javax namespace, websocket-api and websocket-client-api overlap
            "jakarta.websocket:jakarta.websocket-client-api",
            "org.apache.tomcat:tomcat-websocket-api",
            "org.apache.tomcat:tomcat-websocket",
            "org.apache.tomcat.embed:tomcat-embed-websocket",
            "org.eclipse.jetty.toolchain:jetty-javax-websocket-api"
    ),
    JAVAX_WS_RS_API(HIGHEST_VERSION, JavaxWsRsApiRule.class,
            "javax.ws.rs:jsr311-api",
            "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec",
            "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.0_spec",
            "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_1.1_spec",
            "org.jboss.resteasy:jaxrs-api",
            "jakarta.ws.rs:jakarta.ws.rs-api",
            "javax.ws.rs:javax.ws.rs-api"
    ),
    JAVAX_XML_BIND_API(HIGHEST_VERSION, JavaxXmlBindApiRule.class,
            "javax.xml.bind:jaxb-api",
            "jakarta.xml.bind:jakarta.xml.bind-api"
    ),
    JAVAX_XML_WS_API(HIGHEST_VERSION, JavaxXmlWsApiRule.class,
            "javax.xml.ws:jaxws-api",
            "jakarta.xml.ws:jakarta.xml.ws-api"
    ),

    /**
     * Log4J2 has its own implementation with `log4j-core`.
     * It can also delegate to Slf4J with `log4j-to-slf4j`.
     * <p>
     * Given the above:
     * * `log4j-core` and `log4j-to-slf4j` are exclusive
     */
    LOG4J2_IMPL(NONE,
            LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J_CORE.moduleId
    ),

    /**
     * `commons-logging:commons-logging` can be replaced by:
     * * Slf4J with `org.slf4j:jcl-over-slf4j`
     * * Log4J2 with `org.apache.logging.log4j:log4j-jcl` _which requires `commons-logging`_
     * * Spring JCL with `org.springframework:spring-jcl`
     * <p>
     * `commons-logging:commons-logging` can be used from:
     * * Slf4J API delegating to it with `org.slf4j:slf4j-jcl`
     * * Log4J2 API only through Slf4J delegation
     * <p>
     * Given the above:
     * * `jcl-over-slf4j` and `slf4j-jcl` are exclusive
     * * `commons-logging`, `jcl-over-slf4j` and `spring-jcl` are exclusive
     * * `jcl-over-slf4j` and `log4j-jcl` are exclusive
     */
    COMMONS_LOGGING_IMPL(NONE, FixedVersionCapabilityDefinitionRule.class,
            LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SPRING_JCL.moduleId
    ),
    SLF4J_VS_JCL(NONE,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_JCL.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_JCL(NONE, FixedVersionCapabilityDefinitionRule.class,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J_JCL.moduleId
    ),
    /**
     * Log4J2 can act as an Slf4J implementation with `log4j-slf4j-impl` or `log4j-slf4j2-impl`.
     * It can also delegate to Slf4J with `log4j-to-slf4j`.
     * <p>
     * Given the above:
     * * `log4j-slf4j-impl`, `log4j-slf4j2-impl` and `log4j-to-slf4j` are exclusive
     */
    LOG4J2_VS_SLF4J(NONE,
            LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId
    ),
    /**
     * Slf4J provides an API, which requires an implementation.
     * Only one implementation can be on the classpath, selected between:
     * * `slf4j-simple`
     * * `logback-classic`
     * * `slf4j-log4j12` to use Log4J 1.2
     * * `slf4j-jcl` to use Jakarta Commons Logging
     * * `slf4j-jdk14` to use Java Util Logging
     * * `log4j-slf4j-impl` to use Log4J2
     */
    SLF4J_IMPL(NONE, FixedVersionCapabilityDefinitionRule.class,
            LoggingModuleIdentifiers.SLF4J_SIMPLE.moduleId,
            LoggingModuleIdentifiers.LOGBACK_CLASSIC.moduleId,
            LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId,
            LoggingModuleIdentifiers.SLF4J_JCL.moduleId,
            LoggingModuleIdentifiers.SLF4J_JDK14.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId
    ),
    /**
     * `log4j:log4j` can be replaced by:
     * * Slf4j with `log4j-over-slf4j`
     * * Log4J2 with `log4j-1.2-api`
     * <p>
     * Log4J can be used from:
     * * Slf4J API delegating to it with `slf4j-log4j12`
     * * Log4J2 API only through Slf4J delegation
     * <p>
     * Given the above:
     * * `log4j-over-slf4j` and `slf4j-log4j12` are exclusive
     * * `log4j-over-slf4j` and `log4j-1.2-api` and `log4j` are exclusive
     */
    SLF4J_VS_LOG4J(NONE,
            LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_LOG4J(NONE, FixedVersionCapabilityDefinitionRule.class,
            LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J12API.moduleId,
            LoggingModuleIdentifiers.LOG4J.moduleId
    ),
    /**
     * Java Util Logging can be replaced by:
     * * Slf4J with `jul-to-slf4j`
     * * Log4J2 with `log4j-jul`
     * <p>
     * Java Util Logging can be used from:
     * * Slf4J API delegating to it with `slf4j-jdk14`
     * * Log4J2 API only through SLF4J delegation
     * <p>
     * Given the above:
     * * `jul-to-slf4j` and `slf4j-jdk14` are exclusive
     * * `jul-to-slf4j` and `log4j-jul` are exclusive
     */
    SLF4J_VS_JUL(NONE,
            LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_JDK14.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_JUL(NONE, FixedVersionCapabilityDefinitionRule.class,
            LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J_JUL.moduleId
    );

    private final String group;
    private final String capabilityName;
    private final List<String> modules;
    private final DefaultResolutionStrategy defaultStrategy;
    private final Class<? extends CapabilityDefinitionRule> ruleClass;

    CapabilityDefinition(DefaultResolutionStrategy defaultStrategy, String... modules) {
        this(defaultStrategy, "org.gradlex", CapabilityDefinitionRule.class, modules);
    }

    CapabilityDefinition(DefaultResolutionStrategy defaultStrategy, Class<? extends CapabilityDefinitionRule> ruleClass, String... modules) {
        this(defaultStrategy, "org.gradlex", ruleClass, modules);
    }

    CapabilityDefinition(DefaultResolutionStrategy defaultStrategy, String group, Class<? extends CapabilityDefinitionRule> ruleClass, String... modules) {
        this.group = group;
        this.capabilityName = nameInKebabCase();
        this.modules = Arrays.asList(modules);
        this.defaultStrategy = defaultStrategy;
        this.ruleClass = ruleClass;
    }

    public String getCapability() {
        return group + ":" + capabilityName;
    }

    public String getGroup() {
        return group;
    }

    // Not 'getName()' because it may clash with Enum's 'name()' in Kotlin/Groovy
    public String getCapabilityName() {
        return capabilityName;
    }

    public List<String> getModules() {
        return modules;
    }

    public DefaultResolutionStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    public Class<? extends CapabilityDefinitionRule> getRuleClass() {
        return ruleClass;
    }

    private String nameInKebabCase() {
        return name().toLowerCase().replace("_", "-");
    }
}
