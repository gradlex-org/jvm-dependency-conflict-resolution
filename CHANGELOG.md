# JVM Dependency Conflict Resolution Gradle plugin - Changelog

## Version 2.5
* [Adjusted Rule] [#270](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/270) add net.java.dev.jna:jna-jpms and net.java.dev.jna:jna-platform-jpms to JNA rules

## Version 2.4
* [Fix] [#238](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/238) Patch DSL now works for dependencies with non-standard variant names (e.g. com.google.guava).
* [Fix] [#243](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/243) jakarta.xml.ws:jakarta.xml.ws-api no longer clashes with jakarta.jws:jakarta.jws-api for versions <= 4.0
* [Deprecation] [#251](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/251) Deprecate GuavaComponentRule in favor of more general patch DSL.

## Version 2.3
* [New Rule] [#66](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/66) itext:itext / com.lowagie:itext (Thanks [Björn Kautler](https://github.com/Vampire) for reporting)
* [New Rule] [#222](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/222) dk.brics.automaton:automaton / dk.brics:automaton (Thanks [Ketan Padegaonkar](https://github.com/ketan) for reporting)
* [New Rule] [#203](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/203) org.apache.geronimo.javamail:geronimo-javamail_*_provider
* [Adjusted Rule] [#188](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/188) Bouncy Castle rule supports LTS versions (Thanks [Björn Kautler](https://github.com/Vampire) for reporting)
* [Adjusted Rule] [#174](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/174) jakarta.xml.ws-api and jakarta.xml.ws-api merger is now considered (Thanks [KO](https://github.com/ko-at-work) for reporting)
* [Adjusted Rule] [#201](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/201) Add woodstox:wstx-lgpl to WOODSTOX
* [Adjusted Rule] [#202](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/202) Remove org.apache.tomcat:tomcat-websocket from JAVAX_WEBSOCKET_API
* [New] [#229](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/229) Patching: addTargetPlatformVariant optionally accepts a feature name
* [Fix] [#196](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/196) No longer call eager methods on configurations container (Thanks [Ian Brandt](https://github.com/ianbrandt) for reporting)

## Version 2.2
* [New Rule] [#174](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/174) org.elasticsearch:jna / net.java.dev.jna:jna (Thanks [Safeuq Mohamed](https://github.com/safeuq)!)

## Version 2.1.2
* [Adjusted Rule] [#152](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/152) No jakarta-activation-impl capability for early c.sun.a:jakarta.activation (Thanks [lennartfricke](https://github.com/lennartfricke) for reporting)

## Version 2.1.1
* [Fix] [#141](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/141) Jersey alignment rule points to wrong BOM

## Version 2.1
* [New] [#102](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/102) Global consistent resolution feature
* [New Rule] [#125](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/125) mysql:mysql-connector-java / com.mysql:mysql-connector-j (Thanks [Eduardo Acosta Miguens](https://github.com/eduacostam)!)
* [New Rule] [#131](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/131) org.json:json / com.vaadin.external.google:android-json (Thanks [Piotr Kubowicz](https://github.com/pkubowicz)!)
* [New Rule] [#22](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/22) Alignment: Asm, Jersey, Jetty, SSHD (Thanks [Florian Dreier](https://github.com/DreierF)!)
* [Adjusted Rule] [#130](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/130) javax-transaction-api rule now also covers jboss-transaction-api artifacts (Thanks [Piotr Kubowicz](https://github.com/pkubowicz) for reporting)
* [Adjusted Rule] [#140](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/140) Add slf4j-nop to Slf4J implementations
* [Fix] [#132](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/132) Failure when applying plugin with 'listenablefuture' on the classpath (Thanks [Sergii Gnatiuk](https://github.com/ajax-gnatiuk-s) for reporting)

## Version 2.0
* [New] [97](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/97) Unified DSL for conflict resolution, logging and metadata patching
* [New] [78](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/78) Merge with ['dev.jacomet.logging-capabilities' plugin](https://github.com/ljacomet/logging-capabilities)
* [New] [76](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/76) Inform users in case of incompatible RulesMode configuration
* [New Rules] [#96](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/pull/96) Add multiple rules based on 'pom-scijava' (Thanks [Giuseppe Barbieri](https://github.com/elect86)!)

### Breaking changes

* Plugin ID is now `org.gradlex.jvm-dependency-conflict-resolution`
* Minimum required Gradle version raised to 6.8.3

## Version 1.5.2
* [Adjusted Rule] [67](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/67) Remove 'stax2-api' from StaxApiRule

## Version 1.5.1
* [Fix] [#65](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/65) Turn component status rule into a custom rule instead of always applying it to 'all' (Thanks [KO](https://github.com/ko-at-work) for reporting)

## Version 1.5
* [New] [#61](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/61) Generic rule implementations to ease configuration of custom rules in Java projects
* [New Rules] [#63](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/63) Rule that sets the component status to 'integration' for common pre-release version numbers
* [New Rules] [#62](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/62) com.g.c.findbugs:annotations / com.github.spotbugs:spotbugs-annotations (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)

## Version 1.4
* [New Rules] [#50](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/50) net.jcip:jcip-annotations / com.github.stephenc.jcip:jcip-annotations (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [Adjusted Rule] [#41](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/41) org.glassfish.hk2.external:jakarta.inject (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [Adjusted Rule] [#59](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/59) javax.mail:javax.mail-api / org.apache.geronimo.specs:geronimo-javamail (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [Adjusted Rule] [#60](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/60) org.springframework:spring-aop (Thanks [Boris Petrov](https://github.com/boris-petrov)!)

## Version 1.3.1
* [Adjusted Rule] [#54](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/54) Support latest Guava version published with Gradle Metadata (Thanks [Kenny Moens](https://github.com/kmoens) for reporting)

## Version 1.3
* [Adjusted Rule] [#53](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/53) Support Guava 32 and newer (Thanks [MatanSabag](https://github.com/MatanSabag)!)

## Version 1.2
* [New Rules] [#34](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/34) aopalliance:aopalliance / org.springframework:spring-aop
* [New Rules] [#42](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/42) com.intellij:annotations / org.jetbrains:annotations (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [New Rules] [#43](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/43) com.zaxxer:HikariCP (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [Adjusted Rule] [#37](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/37) servletapi:servletapi (Thanks [Kenny Moens](https://github.com/kmoens)!)
* [Adjusted Rule] [#41](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/41) com.jwebmp:javax.inject (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [New] [#47](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/47) Apply 'jvm-ecosystem' plugin

## Version 1.1

* [New Rules] [#20](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/20) net.java.dev.jna:platform (Thanks [Florian Dreier](https://github.com/DreierF)!)
* [New Rules] [#28](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/28) javax.servlet:jsp-api / javax.servlet:jstl
* [New Rules] [#29](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/29) jakarta.activation implementations
* [New Rules] [#31](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/31) org.bouncycastle
* [New Rules] [#32](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/32) javax.json / jakarta.json
* [New Rules] [#33](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/33) javax.websocket / jakarta.websocket
* [Adjusted Rule] [#21](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/21) jakarta.servlet:jakarta.servlet-api (Thanks [Florian Dreier](https://github.com/DreierF)!)
* [Adjusted Rule] [#25](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/25) org.codehaus.woodstox
* [Adjusted Rule] [#26](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/26) javax.activation
* [Adjusted Rule] [#27](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/27) javax.mail

## Version 1.0

* Moved project to [GradleX](https://gradlex.org) - new plugin ID: `org.gradlex.java-ecosystem-capabilities`

## Version 0.7
* [New] [#5](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/5) Allow plugin to be used as Settings Plugin
* [New] [#2](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/2) More rules for Guava - integrated from [missing-metadata-guava](https://github.com/gradlex-org/missing-metadata-guava)
* [New] [#1](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/1) Default resolution strategies can be turned off
* [New] Relax Javax/Jakarta rules, let all rule implementation follow a strict pattern - One rule class per capability

## Version 0.6
* [New] [#10](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/10) Rules added (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [New] [#11](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/11) Rules added (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)

## Version 0.5
* [New] [#9](https://github.com/gradlex-org/jvm-dependency-conflict-resolution/issues/9) Rules added (Thanks [Toldry](https://github.com/Toldry) for contributing)

## Version 0.1 - 0.4
* [New] Initial releases with initial rule set
