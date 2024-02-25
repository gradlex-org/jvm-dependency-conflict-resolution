# Java Ecosystem Capabilities Gradle plugin

[![Build Status](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Fgradlex-org%2Fjava-ecosystem-capabilities%2Fbadge%3Fref%3Dmain&style=flat)](https://actions-badge.atrox.dev/gradlex-org/java-ecosystem-capabilities/goto?ref=main)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin%20Portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Forg%2Fgradlex%2Fjava-ecosystem-capabilities%2Forg.gradlex.java-ecosystem-capabilities.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/org.gradlex.java-ecosystem-capabilities)

This plugin adds [Capabilities](https://docs.gradle.org/current/userguide/component_capabilities.html#capabilities_as_first_level_concept) 
to the metadata of well-known components hosted on Maven Central that are used in many Java projects.

## What is a 'Capability' in Gradle and why should I care?

In the videos below,
I explain the concept of Capability Conflicts and why they can help you to avoid "dependency hell" in your project.
With this plugin, you enable Gradle to detect and automatically resolved typical capability conflicts in the Java Ecosystem.

[<img src="https://onepiecesoftware.github.io/img/videos/29.png" width="320">](https://www.youtube.com/watch?v=KocTqF0hO_8&list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE)
[<img src="https://onepiecesoftware.github.io/img/videos/11.png" width="320">](https://www.youtube.com/watch?v=5g20kbbqBFk&list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE)

## How to use the plugin?

See the [documentation](https://gradlex.org/java-ecosystem-capabilities).

### Supported Gradle versions

- Minimal Gradle version is `6.8.3`

Note: There are rules in this plugin (in particular for _Guava_) which work _better_ with Gradle `7+`.
This is because Gradle 7 added support for the `org.gradle.jvm.environment` attribute that allows Gradle to better distinguish between _standard-jvm_ and _android_ projects.

## Which Components does this plugin affect?

The following list shows all Capabilities and the Components they are added to.
Each Capability's GA coordinates correspond to the GA coordinates of the Component that first introduced the Capability.

<!-- START_GENERATED -->
* com.google.collections:google-collections
  * [com.google.collections:google-collections](https://search.maven.org/artifact/com.google.collections/google-collections)
  * [com.google.guava:guava](https://search.maven.org/artifact/com.google.guava/guava)
* com.google.guava:listenablefuture
  * [com.google.guava:guava](https://search.maven.org/artifact/com.google.guava/guava)
  * [com.google.guava:listenablefuture](https://search.maven.org/artifact/com.google.guava/listenablefuture)
* org.gradlex:aopalliance
  * [aopalliance:aopalliance](https://search.maven.org/artifact/aopalliance/aopalliance)
  * [org.springframework:spring-aop](https://search.maven.org/artifact/org.springframework/spring-aop)
* org.gradlex:apache-csv
  * [org.apache.solr:solr-commons-csv](https://search.maven.org/artifact/org.apache.solr/solr-commons-csv)
  * [org.apache.commons:commons-csv](https://search.maven.org/artifact/org.apache.commons/commons-csv)
* org.gradlex:asm
  * [asm:asm](https://search.maven.org/artifact/asm/asm)
  * [org.ow2.asm:asm](https://search.maven.org/artifact/org.ow2.asm/asm)
* org.gradlex:bouncycastle-bcmail
  * [org.bouncycastle:bcmail](https://search.maven.org/artifact/org.bouncycastle/bcmail)
  * [org.bouncycastle:bcmail-fips](https://search.maven.org/artifact/org.bouncycastle/bcmail-fips)
  * [org.bouncycastle:bcmail-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk14)
  * [org.bouncycastle:bcmail-jdk15](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk15)
  * [org.bouncycastle:bcmail-jdk15+](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk15+)
  * [org.bouncycastle:bcmail-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk15on)
  * [org.bouncycastle:bcmail-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk15to18)
  * [org.bouncycastle:bcmail-jdk16](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk16)
  * [org.bouncycastle:bcmail-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcmail-jdk18on)
  * [org.bouncycastle:bcjmail-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcjmail-jdk15on)
  * [org.bouncycastle:bcjmail-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcjmail-jdk15to18)
  * [org.bouncycastle:bcjmail-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcjmail-jdk18on)
* org.gradlex:bouncycastle-bcpg
  * [org.bouncycastle:bcpg](https://search.maven.org/artifact/org.bouncycastle/bcpg)
  * [org.bouncycastle:bcpg-fips](https://search.maven.org/artifact/org.bouncycastle/bcpg-fips)
  * [org.bouncycastle:bcpg-jdk12](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk12)
  * [org.bouncycastle:bcpg-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk14)
  * [org.bouncycastle:bcpg-jdk15](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk15)
  * [org.bouncycastle:bcpg-jdk15+](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk15+)
  * [org.bouncycastle:bcpg-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk15on)
  * [org.bouncycastle:bcpg-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk15to18)
  * [org.bouncycastle:bcpg-jdk16](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk16)
  * [org.bouncycastle:bcpg-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcpg-jdk18on)
* org.gradlex:bouncycastle-bcpkix
  * [org.bouncycastle:bcpkix](https://search.maven.org/artifact/org.bouncycastle/bcpkix)
  * [org.bouncycastle:bcpkix-fips](https://search.maven.org/artifact/org.bouncycastle/bcpkix-fips)
  * [org.bouncycastle:bcpkix-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcpkix-jdk14)
  * [org.bouncycastle:bcpkix-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcpkix-jdk15on)
  * [org.bouncycastle:bcpkix-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcpkix-jdk15to18)
  * [org.bouncycastle:bcpkix-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcpkix-jdk18on)
* org.gradlex:bouncycastle-bcprov
  * [org.bouncycastle:bcprov](https://search.maven.org/artifact/org.bouncycastle/bcprov)
  * [org.bouncycastle:bcprov-debug-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcprov-debug-jdk14)
  * [org.bouncycastle:bcprov-debug-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcprov-debug-jdk15on)
  * [org.bouncycastle:bcprov-debug-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcprov-debug-jdk15to18)
  * [org.bouncycastle:bcprov-debug-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcprov-debug-jdk18on)
  * [org.bouncycastle:bcprov-ext-debug-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-debug-jdk14)
  * [org.bouncycastle:bcprov-ext-debug-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-debug-jdk15on)
  * [org.bouncycastle:bcprov-ext-debug-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-debug-jdk15to18)
  * [org.bouncycastle:bcprov-ext-debug-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-debug-jdk18on)
  * [org.bouncycastle:bcprov-ext-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk14)
  * [org.bouncycastle:bcprov-ext-jdk15](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk15)
  * [org.bouncycastle:bcprov-ext-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk15on)
  * [org.bouncycastle:bcprov-ext-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk15to18)
  * [org.bouncycastle:bcprov-ext-jdk16](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk16)
  * [org.bouncycastle:bcprov-ext-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcprov-ext-jdk18on)
  * [org.bouncycastle:bcprov-jdk12](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk12)
  * [org.bouncycastle:bcprov-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk14)
  * [org.bouncycastle:bcprov-jdk15](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk15)
  * [org.bouncycastle:bcprov-jdk15+](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk15+)
  * [org.bouncycastle:bcprov-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk15on)
  * [org.bouncycastle:bcprov-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk15to18)
  * [org.bouncycastle:bcprov-jdk16](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk16)
  * [org.bouncycastle:bcprov-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcprov-jdk18on)
  * [org.bouncycastle:bc-fips](https://search.maven.org/artifact/org.bouncycastle/bc-fips)
  * [org.bouncycastle:bc-fips-debug](https://search.maven.org/artifact/org.bouncycastle/bc-fips-debug)
* org.gradlex:bouncycastle-bctls
  * [org.bouncycastle:bctls](https://search.maven.org/artifact/org.bouncycastle/bctls)
  * [org.bouncycastle:bctls-jdk14](https://search.maven.org/artifact/org.bouncycastle/bctls-jdk14)
  * [org.bouncycastle:bctls-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bctls-jdk15on)
  * [org.bouncycastle:bctls-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bctls-jdk15to18)
  * [org.bouncycastle:bctls-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bctls-jdk18on)
  * [org.bouncycastle:bctls-fips](https://search.maven.org/artifact/org.bouncycastle/bctls-fips)
* org.gradlex:bouncycastle-bctsp
  * [org.bouncycastle:bctsp](https://search.maven.org/artifact/org.bouncycastle/bctsp)
  * [org.bouncycastle:bctsp-jdk14](https://search.maven.org/artifact/org.bouncycastle/bctsp-jdk14)
  * [org.bouncycastle:bctsp-jdk15](https://search.maven.org/artifact/org.bouncycastle/bctsp-jdk15)
  * [org.bouncycastle:bctsp-jdk15+](https://search.maven.org/artifact/org.bouncycastle/bctsp-jdk15+)
  * [org.bouncycastle:bctsp-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bctsp-jdk15on)
  * [org.bouncycastle:bctsp-jdk16](https://search.maven.org/artifact/org.bouncycastle/bctsp-jdk16)
* org.gradlex:bouncycastle-bcutil
  * [org.bouncycastle:bcutil](https://search.maven.org/artifact/org.bouncycastle/bcutil)
  * [org.bouncycastle:bcutil-jdk14](https://search.maven.org/artifact/org.bouncycastle/bcutil-jdk14)
  * [org.bouncycastle:bcutil-jdk15on](https://search.maven.org/artifact/org.bouncycastle/bcutil-jdk15on)
  * [org.bouncycastle:bcutil-jdk15to18](https://search.maven.org/artifact/org.bouncycastle/bcutil-jdk15to18)
  * [org.bouncycastle:bcutil-jdk18on](https://search.maven.org/artifact/org.bouncycastle/bcutil-jdk18on)
* org.gradlex:c3p0
  * [c3p0:c3p0](https://search.maven.org/artifact/c3p0/c3p0)
  * [com.mchange:c3p0](https://search.maven.org/artifact/com.mchange/c3p0)
* org.gradlex:cglib
  * [cglib:cglib](https://search.maven.org/artifact/cglib/cglib)
  * [cglib:cglib-nodep](https://search.maven.org/artifact/cglib/cglib-nodep)
* org.gradlex:commons-beanutils
  * [commons-beanutils:commons-beanutils-core](https://search.maven.org/artifact/commons-beanutils/commons-beanutils-core)
  * [commons-beanutils:commons-beanutils](https://search.maven.org/artifact/commons-beanutils/commons-beanutils)
* org.gradlex:commons-io
  * [commons-io:commons-io](https://search.maven.org/artifact/commons-io/commons-io)
  * [org.apache.commons:commons-io](https://search.maven.org/artifact/org.apache.commons/commons-io)
* org.gradlex:commons-logging-impl
  * [commons-logging:commons-logging](https://search.maven.org/artifact/commons-logging/commons-logging)
  * [org.slf4j:jcl-over-slf4j](https://search.maven.org/artifact/org.slf4j/jcl-over-slf4j)
  * [org.springframework:spring-jcl](https://search.maven.org/artifact/org.springframework/spring-jcl)
* org.gradlex:dom4j
  * [dom4j:dom4j](https://search.maven.org/artifact/dom4j/dom4j)
  * [org.dom4j:dom4j](https://search.maven.org/artifact/org.dom4j/dom4j)
* org.gradlex:findbugs-annotations
  * [com.google.code.findbugs:annotations](https://search.maven.org/artifact/com.google.code.findbugs/annotations)
  * [com.google.code.findbugs:findbugs-annotations](https://search.maven.org/artifact/com.google.code.findbugs/findbugs-annotations)
  * [com.github.spotbugs:spotbugs-annotations](https://search.maven.org/artifact/com.github.spotbugs/spotbugs-annotations)
* org.gradlex:guava
  * [com.google.guava:guava](https://search.maven.org/artifact/com.google.guava/guava)
  * [com.google.guava:guava-jdk5](https://search.maven.org/artifact/com.google.guava/guava-jdk5)
* org.gradlex:hamcrest-core
  * [org.hamcrest:hamcrest](https://search.maven.org/artifact/org.hamcrest/hamcrest)
  * [org.hamcrest:hamcrest-core](https://search.maven.org/artifact/org.hamcrest/hamcrest-core)
* org.gradlex:hamcrest-library
  * [org.hamcrest:hamcrest](https://search.maven.org/artifact/org.hamcrest/hamcrest)
  * [org.hamcrest:hamcrest-library](https://search.maven.org/artifact/org.hamcrest/hamcrest-library)
* org.gradlex:hikari-cp
  * [com.zaxxer:HikariCP](https://search.maven.org/artifact/com.zaxxer/HikariCP)
  * [com.zaxxer:HikariCP-java6](https://search.maven.org/artifact/com.zaxxer/HikariCP-java6)
  * [com.zaxxer:HikariCP-java7](https://search.maven.org/artifact/com.zaxxer/HikariCP-java7)
* org.gradlex:intellij-annotations
  * [org.jetbrains:annotations](https://search.maven.org/artifact/org.jetbrains/annotations)
  * [com.intellij:annotations](https://search.maven.org/artifact/com.intellij/annotations)
* org.gradlex:jakarta-activation-api
  * [jakarta.activation:jakarta.activation-api](https://search.maven.org/artifact/jakarta.activation/jakarta.activation-api)
  * [com.sun.activation:jakarta.activation](https://search.maven.org/artifact/com.sun.activation/jakarta.activation)
* org.gradlex:jakarta-activation-impl
  * [com.sun.activation:jakarta.activation](https://search.maven.org/artifact/com.sun.activation/jakarta.activation)
  * [org.eclipse.angus:angus-activation](https://search.maven.org/artifact/org.eclipse.angus/angus-activation)
* org.gradlex:jakarta-annotation-api
  * [jakarta.annotation:jakarta.annotation-api](https://search.maven.org/artifact/jakarta.annotation/jakarta.annotation-api)
  * [org.apache.tomcat:tomcat-annotations-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-annotations-api)
* org.gradlex:jakarta-json-api
  * [jakarta.json:jakarta.json-api](https://search.maven.org/artifact/jakarta.json/jakarta.json-api)
  * [org.glassfish:jakarta.json](https://search.maven.org/artifact/org.glassfish/jakarta.json)
* org.gradlex:jakarta-mail-api
  * [jakarta.mail:jakarta.mail-api](https://search.maven.org/artifact/jakarta.mail/jakarta.mail-api)
  * [com.sun.mail:mailapi](https://search.maven.org/artifact/com.sun.mail/mailapi)
  * [com.sun.mail:jakarta.mail](https://search.maven.org/artifact/com.sun.mail/jakarta.mail)
  * [org.eclipse.angus:jakarta.mail](https://search.maven.org/artifact/org.eclipse.angus/jakarta.mail)
* org.gradlex:jakarta-servlet-api
  * [jakarta.servlet:jakarta.servlet-api](https://search.maven.org/artifact/jakarta.servlet/jakarta.servlet-api)
  * [org.apache.tomcat:tomcat-servlet-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-servlet-api)
  * [org.apache.tomcat.embed:tomcat-embed-core](https://search.maven.org/artifact/org.apache.tomcat.embed/tomcat-embed-core)
  * [org.eclipse.jetty.toolchain:jetty-jakarta-servlet-api](https://search.maven.org/artifact/org.eclipse.jetty.toolchain/jetty-jakarta-servlet-api)
* org.gradlex:jakarta-websocket-api
  * [jakarta.websocket:jakarta.websocket-api](https://search.maven.org/artifact/jakarta.websocket/jakarta.websocket-api)
  * [org.apache.tomcat:tomcat-websocket-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket-api)
  * [org.apache.tomcat:tomcat-websocket](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket)
  * [org.apache.tomcat.embed:tomcat-embed-websocket](https://search.maven.org/artifact/org.apache.tomcat.embed/tomcat-embed-websocket)
  * [org.eclipse.jetty.toolchain:jetty-jakarta-websocket-api](https://search.maven.org/artifact/org.eclipse.jetty.toolchain/jetty-jakarta-websocket-api)
* org.gradlex:jakarta-websocket-client-api
  * [jakarta.websocket:jakarta.websocket-client-api](https://search.maven.org/artifact/jakarta.websocket/jakarta.websocket-client-api)
  * [org.apache.tomcat:tomcat-websocket-client-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket-client-api)
  * [org.apache.tomcat:tomcat-websocket](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket)
  * [org.apache.tomcat.embed:tomcat-embed-websocket](https://search.maven.org/artifact/org.apache.tomcat.embed/tomcat-embed-websocket)
  * [org.eclipse.jetty.toolchain:jetty-jakarta-websocket-api](https://search.maven.org/artifact/org.eclipse.jetty.toolchain/jetty-jakarta-websocket-api)
* org.gradlex:jakarta-ws-rs-api
  * [jakarta.ws.rs:jakarta.ws.rs-api](https://search.maven.org/artifact/jakarta.ws.rs/jakarta.ws.rs-api)
  * [org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_3.0_spec](https://search.maven.org/artifact/org.jboss.spec.javax.ws.rs/jboss-jaxrs-api_3.0_spec)
* org.gradlex:java-assist
  * [javassist:javassist](https://search.maven.org/artifact/javassist/javassist)
  * [org.javassist:javassist](https://search.maven.org/artifact/org.javassist/javassist)
  * [jboss:javassist](https://search.maven.org/artifact/jboss/javassist)
* org.gradlex:javax-activation-api
  * [javax.activation:activation](https://search.maven.org/artifact/javax.activation/activation)
  * [jakarta.activation:jakarta.activation-api](https://search.maven.org/artifact/jakarta.activation/jakarta.activation-api)
  * [javax.activation:javax.activation-api](https://search.maven.org/artifact/javax.activation/javax.activation-api)
  * [com.sun.activation:javax.activation](https://search.maven.org/artifact/com.sun.activation/javax.activation)
  * [com.sun.activation:jakarta.activation](https://search.maven.org/artifact/com.sun.activation/jakarta.activation)
* org.gradlex:javax-annotation-api
  * [javax.annotation:jsr250-api](https://search.maven.org/artifact/javax.annotation/jsr250-api)
  * [jakarta.annotation:jakarta.annotation-api](https://search.maven.org/artifact/jakarta.annotation/jakarta.annotation-api)
  * [javax.annotation:javax.annotation-api](https://search.maven.org/artifact/javax.annotation/javax.annotation-api)
  * [org.apache.tomcat:tomcat-annotations-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-annotations-api)
* org.gradlex:javax-ejb-api
  * [javax.ejb:ejb](https://search.maven.org/artifact/javax.ejb/ejb)
  * [jakarta.ejb:jakarta.ejb-api](https://search.maven.org/artifact/jakarta.ejb/jakarta.ejb-api)
  * [javax.ejb:javax.ejb-api](https://search.maven.org/artifact/javax.ejb/javax.ejb-api)
  * [javax.ejb:ejb-api](https://search.maven.org/artifact/javax.ejb/ejb-api)
* org.gradlex:javax-el-api
  * [javax.el:el-api](https://search.maven.org/artifact/javax.el/el-api)
  * [jakarta.el:jakarta.el-api](https://search.maven.org/artifact/jakarta.el/jakarta.el-api)
  * [javax.el:javax.el-api](https://search.maven.org/artifact/javax.el/javax.el-api)
* org.gradlex:javax-inject-api
  * [javax.inject:javax.inject](https://search.maven.org/artifact/javax.inject/javax.inject)
  * [jakarta.inject:jakarta.inject-api](https://search.maven.org/artifact/jakarta.inject/jakarta.inject-api)
  * [com.jwebmp:javax.inject](https://search.maven.org/artifact/com.jwebmp/javax.inject)
  * [org.glassfish.hk2.external:javax.inject](https://search.maven.org/artifact/org.glassfish.hk2.external/javax.inject)
  * [org.glassfish.hk2.external:jakarta.inject](https://search.maven.org/artifact/org.glassfish.hk2.external/jakarta.inject)
* org.gradlex:javax-json-api
  * [javax.json:javax.json-api](https://search.maven.org/artifact/javax.json/javax.json-api)
  * [jakarta.json:jakarta.json-api](https://search.maven.org/artifact/jakarta.json/jakarta.json-api)
  * [org.glassfish:javax.json](https://search.maven.org/artifact/org.glassfish/javax.json)
  * [org.glassfish:jakarta.json](https://search.maven.org/artifact/org.glassfish/jakarta.json)
* org.gradlex:javax-jws-api
  * [javax.jws:javax.jws-api](https://search.maven.org/artifact/javax.jws/javax.jws-api)
  * [jakarta.jws:jakarta.jws-api](https://search.maven.org/artifact/jakarta.jws/jakarta.jws-api)
* org.gradlex:javax-mail-api
  * [com.sun.mail:mailapi](https://search.maven.org/artifact/com.sun.mail/mailapi)
  * [jakarta.mail:jakarta.mail-api](https://search.maven.org/artifact/jakarta.mail/jakarta.mail-api)
  * [javax.mail:javax.mail-api](https://search.maven.org/artifact/javax.mail/javax.mail-api)
  * [javax.mail:mail](https://search.maven.org/artifact/javax.mail/mail)
  * [com.sun.mail:javax.mail](https://search.maven.org/artifact/com.sun.mail/javax.mail)
  * [com.sun.mail:jakarta.mail](https://search.maven.org/artifact/com.sun.mail/jakarta.mail)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.3.1_mail](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.3.1_mail)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.3.1_provider](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.3.1_provider)
  * [org.apache.geronimo.specs:geronimo-javamail_1.3.1_spec](https://search.maven.org/artifact/org.apache.geronimo.specs/geronimo-javamail_1.3.1_spec)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.4_mail](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.4_mail)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.4_provider](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.4_provider)
  * [org.apache.geronimo.specs:geronimo-javamail_1.4_spec](https://search.maven.org/artifact/org.apache.geronimo.specs/geronimo-javamail_1.4_spec)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.6_mail](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.6_mail)
  * [org.apache.geronimo.javamail:geronimo-javamail_1.6_provider](https://search.maven.org/artifact/org.apache.geronimo.javamail/geronimo-javamail_1.6_provider)
  * [org.apache.geronimo.specs:geronimo-javamail_1.6_spec](https://search.maven.org/artifact/org.apache.geronimo.specs/geronimo-javamail_1.6_spec)
* org.gradlex:javax-persistence-api
  * [javax.persistence:javax.persistence-api](https://search.maven.org/artifact/javax.persistence/javax.persistence-api)
  * [jakarta.persistence:jakarta.persistence-api](https://search.maven.org/artifact/jakarta.persistence/jakarta.persistence-api)
  * [org.hibernate.javax.persistence:hibernate-jpa-2.2-api](https://search.maven.org/artifact/org.hibernate.javax.persistence/hibernate-jpa-2.2-api)
  * [org.hibernate.javax.persistence:hibernate-jpa-2.1-api](https://search.maven.org/artifact/org.hibernate.javax.persistence/hibernate-jpa-2.1-api)
  * [org.hibernate.javax.persistence:hibernate-jpa-2.0-api](https://search.maven.org/artifact/org.hibernate.javax.persistence/hibernate-jpa-2.0-api)
* org.gradlex:javax-servlet-api
  * [javax.servlet:servlet-api](https://search.maven.org/artifact/javax.servlet/servlet-api)
  * [javax.servlet:javax.servlet-api](https://search.maven.org/artifact/javax.servlet/javax.servlet-api)
  * [jakarta.servlet:jakarta.servlet-api](https://search.maven.org/artifact/jakarta.servlet/jakarta.servlet-api)
  * [org.apache.tomcat:servlet-api](https://search.maven.org/artifact/org.apache.tomcat/servlet-api)
  * [org.apache.tomcat:tomcat-servlet-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-servlet-api)
  * [org.apache.tomcat.embed:tomcat-embed-core](https://search.maven.org/artifact/org.apache.tomcat.embed/tomcat-embed-core)
  * [servletapi:servletapi](https://search.maven.org/artifact/servletapi/servletapi)
* org.gradlex:javax-servlet-jsp
  * [javax.servlet:jsp-api](https://search.maven.org/artifact/javax.servlet/jsp-api)
  * [javax.servlet.jsp:jsp-api](https://search.maven.org/artifact/javax.servlet.jsp/jsp-api)
  * [javax.servlet.jsp:javax.servlet.jsp-api](https://search.maven.org/artifact/javax.servlet.jsp/javax.servlet.jsp-api)
  * [jakarta.servlet.jsp:jakarta.servlet.jsp-api](https://search.maven.org/artifact/jakarta.servlet.jsp/jakarta.servlet.jsp-api)
* org.gradlex:javax-servlet-jstl
  * [javax.servlet:jstl](https://search.maven.org/artifact/javax.servlet/jstl)
  * [javax.servlet.jsp.jstl:jstl-api](https://search.maven.org/artifact/javax.servlet.jsp.jstl/jstl-api)
  * [javax.servlet.jsp.jstl:javax.servlet.jsp.jstl-api](https://search.maven.org/artifact/javax.servlet.jsp.jstl/javax.servlet.jsp.jstl-api)
  * [jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api](https://search.maven.org/artifact/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api)
* org.gradlex:javax-soap-api
  * [javax.xml.soap:javax.xml.soap-api](https://search.maven.org/artifact/javax.xml.soap/javax.xml.soap-api)
  * [jakarta.xml.soap:jakarta.xml.soap-api](https://search.maven.org/artifact/jakarta.xml.soap/jakarta.xml.soap-api)
* org.gradlex:javax-transaction-api
  * [javax.transaction:jta](https://search.maven.org/artifact/javax.transaction/jta)
  * [javax.transaction:javax.transaction-api](https://search.maven.org/artifact/javax.transaction/javax.transaction-api)
  * [jakarta.transaction:jakarta.transaction-api](https://search.maven.org/artifact/jakarta.transaction/jakarta.transaction-api)
* org.gradlex:javax-validation-api
  * [javax.validation:validation-api](https://search.maven.org/artifact/javax.validation/validation-api)
  * [jakarta.validation:jakarta.validation-api](https://search.maven.org/artifact/jakarta.validation/jakarta.validation-api)
* org.gradlex:javax-websocket-api-rule
  * [javax.websocket:javax.websocket-api](https://search.maven.org/artifact/javax.websocket/javax.websocket-api)
  * [jakarta.websocket:jakarta.websocket-api](https://search.maven.org/artifact/jakarta.websocket/jakarta.websocket-api)
  * [javax.websocket:javax.websocket-client-api](https://search.maven.org/artifact/javax.websocket/javax.websocket-client-api)
  * [jakarta.websocket:jakarta.websocket-client-api](https://search.maven.org/artifact/jakarta.websocket/jakarta.websocket-client-api)
  * [org.apache.tomcat:tomcat-websocket-api](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket-api)
  * [org.apache.tomcat:tomcat-websocket](https://search.maven.org/artifact/org.apache.tomcat/tomcat-websocket)
  * [org.apache.tomcat.embed:tomcat-embed-websocket](https://search.maven.org/artifact/org.apache.tomcat.embed/tomcat-embed-websocket)
  * [org.eclipse.jetty.toolchain:jetty-javax-websocket-api](https://search.maven.org/artifact/org.eclipse.jetty.toolchain/jetty-javax-websocket-api)
* org.gradlex:javax-ws-rs-api
  * [javax.ws.rs:jsr311-api](https://search.maven.org/artifact/javax.ws.rs/jsr311-api)
  * [org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec](https://search.maven.org/artifact/org.jboss.spec.javax.ws.rs/jboss-jaxrs-api_2.1_spec)
  * [org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.0_spec](https://search.maven.org/artifact/org.jboss.spec.javax.ws.rs/jboss-jaxrs-api_2.0_spec)
  * [org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_1.1_spec](https://search.maven.org/artifact/org.jboss.spec.javax.ws.rs/jboss-jaxrs-api_1.1_spec)
  * [org.jboss.resteasy:jaxrs-api](https://search.maven.org/artifact/org.jboss.resteasy/jaxrs-api)
  * [jakarta.ws.rs:jakarta.ws.rs-api](https://search.maven.org/artifact/jakarta.ws.rs/jakarta.ws.rs-api)
  * [javax.ws.rs:javax.ws.rs-api](https://search.maven.org/artifact/javax.ws.rs/javax.ws.rs-api)
* org.gradlex:javax-xml-bind-api
  * [javax.xml.bind:jaxb-api](https://search.maven.org/artifact/javax.xml.bind/jaxb-api)
  * [jakarta.xml.bind:jakarta.xml.bind-api](https://search.maven.org/artifact/jakarta.xml.bind/jakarta.xml.bind-api)
* org.gradlex:javax-xml-ws-api
  * [javax.xml.ws:jaxws-api](https://search.maven.org/artifact/javax.xml.ws/jaxws-api)
  * [jakarta.xml.ws:jakarta.xml.ws-api](https://search.maven.org/artifact/jakarta.xml.ws/jakarta.xml.ws-api)
* org.gradlex:jcip-annotations
  * [net.jcip:jcip-annotations](https://search.maven.org/artifact/net.jcip/jcip-annotations)
  * [com.github.stephenc.jcip:jcip-annotations](https://search.maven.org/artifact/com.github.stephenc.jcip/jcip-annotations)
* org.gradlex:jna-platform
  * [net.java.dev.jna:platform](https://search.maven.org/artifact/net.java.dev.jna/platform)
  * [net.java.dev.jna:jna-platform](https://search.maven.org/artifact/net.java.dev.jna/jna-platform)
* org.gradlex:jts-core
  * [com.vividsolutions:jts](https://search.maven.org/artifact/com.vividsolutions/jts)
  * [com.vividsolutions:jts-core](https://search.maven.org/artifact/com.vividsolutions/jts-core)
* org.gradlex:junit
  * [junit:junit](https://search.maven.org/artifact/junit/junit)
  * [junit:junit-dep](https://search.maven.org/artifact/junit/junit-dep)
* org.gradlex:jzy3d-emul-gl
  * [org.jzy3d:jzy3d-emul-gl](https://search.maven.org/artifact/org.jzy3d/jzy3d-emul-gl)
  * [org.jzy3d:jzy3d-emul-gl-awt](https://search.maven.org/artifact/org.jzy3d/jzy3d-emul-gl-awt)
* org.gradlex:jzy3d-jgl
  * [org.jzy3d:jGL](https://search.maven.org/artifact/org.jzy3d/jGL)
  * [org.jzy3d:jzy3d-jGL-awt](https://search.maven.org/artifact/org.jzy3d/jzy3d-jGL-awt)
* org.gradlex:log4j2-impl
  * [org.apache.logging.log4j:log4j-to-slf4j](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-to-slf4j)
  * [org.apache.logging.log4j:log4j-core](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-core)
* org.gradlex:log4j2-vs-slf4j
  * [org.apache.logging.log4j:log4j-slf4j-impl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-slf4j-impl)
  * [org.apache.logging.log4j:log4j-slf4j2-impl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-slf4j2-impl)
  * [org.apache.logging.log4j:log4j-to-slf4j](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-to-slf4j)
* org.gradlex:lz4
  * [net.jpountz.lz4:lz4](https://search.maven.org/artifact/net.jpountz.lz4/lz4)
  * [org.lz4:lz4-java](https://search.maven.org/artifact/org.lz4/lz4-java)
* org.gradlex:mchange-commons-java
  * [c3p0:c3p0](https://search.maven.org/artifact/c3p0/c3p0)
  * [com.mchange:mchange-commons-java](https://search.maven.org/artifact/com.mchange/mchange-commons-java)
* org.gradlex:miglayout
  * [com.miglayout:miglayout-swing](https://search.maven.org/artifact/com.miglayout/miglayout-swing)
  * [com.miglayout:miglayout](https://search.maven.org/artifact/com.miglayout/miglayout)
* org.gradlex:postgresql
  * [postgresql:postgresql](https://search.maven.org/artifact/postgresql/postgresql)
  * [org.postgresql:postgresql](https://search.maven.org/artifact/org.postgresql/postgresql)
* org.gradlex:slf4j-impl
  * [org.slf4j:slf4j-simple](https://search.maven.org/artifact/org.slf4j/slf4j-simple)
  * [ch.qos.logback:logback-classic](https://search.maven.org/artifact/ch.qos.logback/logback-classic)
  * [org.slf4j:slf4j-log4j12](https://search.maven.org/artifact/org.slf4j/slf4j-log4j12)
  * [org.slf4j:slf4j-jcl](https://search.maven.org/artifact/org.slf4j/slf4j-jcl)
  * [org.slf4j:slf4j-jdk14](https://search.maven.org/artifact/org.slf4j/slf4j-jdk14)
  * [org.apache.logging.log4j:log4j-slf4j-impl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-slf4j-impl)
  * [org.apache.logging.log4j:log4j-slf4j2-impl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-slf4j2-impl)
* org.gradlex:slf4j-vs-jcl
  * [org.slf4j:jcl-over-slf4j](https://search.maven.org/artifact/org.slf4j/jcl-over-slf4j)
  * [org.slf4j:slf4j-jcl](https://search.maven.org/artifact/org.slf4j/slf4j-jcl)
* org.gradlex:slf4j-vs-jul
  * [org.slf4j:jul-to-slf4j](https://search.maven.org/artifact/org.slf4j/jul-to-slf4j)
  * [org.slf4j:slf4j-jdk14](https://search.maven.org/artifact/org.slf4j/slf4j-jdk14)
* org.gradlex:slf4j-vs-log4j
  * [org.slf4j:log4j-over-slf4j](https://search.maven.org/artifact/org.slf4j/log4j-over-slf4j)
  * [org.slf4j:slf4j-log4j12](https://search.maven.org/artifact/org.slf4j/slf4j-log4j12)
* org.gradlex:slf4j-vs-log4j2-for-jcl
  * [org.slf4j:jcl-over-slf4j](https://search.maven.org/artifact/org.slf4j/jcl-over-slf4j)
  * [org.apache.logging.log4j:log4j-jcl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-jcl)
* org.gradlex:slf4j-vs-log4j2-for-jul
  * [org.slf4j:jul-to-slf4j](https://search.maven.org/artifact/org.slf4j/jul-to-slf4j)
  * [org.apache.logging.log4j:log4j-jul](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-jul)
* org.gradlex:slf4j-vs-log4j2-for-log4j
  * [org.slf4j:log4j-over-slf4j](https://search.maven.org/artifact/org.slf4j/log4j-over-slf4j)
  * [org.apache.logging.log4j:log4j-1.2-api](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-1.2-api)
  * [log4j:log4j](https://search.maven.org/artifact/log4j/log4j)
* org.gradlex:stax-api
  * [stax:stax-api](https://search.maven.org/artifact/stax/stax-api)
  * [javax.xml.stream:stax-api](https://search.maven.org/artifact/javax.xml.stream/stax-api)
* org.gradlex:velocity
  * [velocity:velocity](https://search.maven.org/artifact/velocity/velocity)
  * [org.apache.velocity:velocity](https://search.maven.org/artifact/org.apache.velocity/velocity)
  * [org.apache.velocity:velocity-engine-core](https://search.maven.org/artifact/org.apache.velocity/velocity-engine-core)
* org.gradlex:woodstox-asl
  * [org.codehaus.woodstox:woodstox-core-asl](https://search.maven.org/artifact/org.codehaus.woodstox/woodstox-core-asl)
  * [org.codehaus.woodstox:woodstox-core-lgpl](https://search.maven.org/artifact/org.codehaus.woodstox/woodstox-core-lgpl)
  * [org.codehaus.woodstox:wstx-asl](https://search.maven.org/artifact/org.codehaus.woodstox/wstx-asl)
  * [org.codehaus.woodstox:wstx-lgpl](https://search.maven.org/artifact/org.codehaus.woodstox/wstx-lgpl)
  * [woodstox:wstx-asl](https://search.maven.org/artifact/woodstox/wstx-asl)

<!-- END_GENERATED -->

# Disclaimer

Gradle and the Gradle logo are trademarks of Gradle, Inc.
The GradleX project is not endorsed by, affiliated with, or associated with Gradle or Gradle, Inc. in any way.
