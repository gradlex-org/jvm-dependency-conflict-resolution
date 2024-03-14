# Java Ecosystem Capabilities Gradle plugin - Changelog

## Version 2.0
* [New] [76](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/76) Inform users in case of incompatible RulesMode configuration
* [New Rules] [78](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/78) Merge with ['dev.jacomet.logging-capabilities' plugin](https://github.com/ljacomet/logging-capabilities)
* [New Rules] [#96](https://github.com/gradlex-org/java-ecosystem-capabilities/pull/96) Add multiple rules based on 'pom-scijava' (Thanks [Giuseppe Barbieri](https://github.com/elect86)!)

### Breaking changes

* Minimum required Gradle version raised to 6.8.3

## Version 1.5.2
* [Adjusted Rule] [67](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/67) Remove 'stax2-api' from StaxApiRule

## Version 1.5.1
* [Fix] [#65](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/65) Turn component status rule into a custom rule instead of always applying it to 'all' (Thanks [KO](https://github.com/ko-at-work) for reporting)

## Version 1.5
* [New] [#61](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/61) Generic rule implementations to ease configuration of custom rules in Java projects
* [New Rules] [#63](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/63) Rule that sets the component status to 'integration' for common pre-release version numbers
* [New Rules] [#62](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/62) com.g.c.findbugs:annotations / com.github.spotbugs:spotbugs-annotations (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)

## Version 1.4
* [New Rules] [#50](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/50) net.jcip:jcip-annotations / com.github.stephenc.jcip:jcip-annotations (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [Adjusted Rule] [#41](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/41) org.glassfish.hk2.external:jakarta.inject (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [Adjusted Rule] [#59](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/59) javax.mail:javax.mail-api / org.apache.geronimo.specs:geronimo-javamail (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [Adjusted Rule] [#60](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/60) org.springframework:spring-aop (Thanks [Boris Petrov](https://github.com/boris-petrov)!)

## Version 1.3.1
* [Adjusted Rule] [#54](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/54) Support latest Guava version published with Gradle Metadata (Thanks [Kenny Moens](https://github.com/kmoens) for reporting)

## Version 1.3
* [Adjusted Rule] [#53](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/53) Support Guava 32 and newer (Thanks [MatanSabag](https://github.com/MatanSabag)!)

## Version 1.2
* [New Rules] [#34](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/34) aopalliance:aopalliance / org.springframework:spring-aop
* [New Rules] [#42](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/42) com.intellij:annotations / org.jetbrains:annotations (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [New Rules] [#43](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/43) com.zaxxer:HikariCP (Thanks [Boris Petrov](https://github.com/boris-petrov)!)
* [Adjusted Rule] [#37](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/37) servletapi:servletapi (Thanks [Kenny Moens](https://github.com/kmoens)!)
* [Adjusted Rule] [#41](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/41) com.jwebmp:javax.inject (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [New] [#47](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/47) Apply 'jvm-ecosystem' plugin

## Version 1.1

* [New Rules] [#20](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/20) net.java.dev.jna:platform (Thanks [Florian Dreier](https://github.com/DreierF)!)
* [New Rules] [#28](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/28) javax.servlet:jsp-api / javax.servlet:jstl
* [New Rules] [#29](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/29) jakarta.activation implementations
* [New Rules] [#31](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/31) org.bouncycastle
* [New Rules] [#32](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/32) javax.json / jakarta.json
* [New Rules] [#33](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/33) javax.websocket / jakarta.websocket
* [Adjusted Rule] [#21](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/21) jakarta.servlet:jakarta.servlet-api (Thanks [Florian Dreier](https://github.com/DreierF)!)
* [Adjusted Rule] [#25](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/25) org.codehaus.woodstox
* [Adjusted Rule] [#26](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/26) javax.activation
* [Adjusted Rule] [#27](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/27) javax.mail

## Version 1.0

* Moved project to [GradleX](https://gradlex.org) - new plugin ID: `org.gradlex.java-ecosystem-capabilities`

## Version 0.7
* [New] [#5](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/5) Allow plugin to be used as Settings Plugin
* [New] [#2](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/2) More rules for Guava - integrated from [missing-metadata-guava](https://github.com/gradlex-org/missing-metadata-guava)
* [New] [#1](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/1) Default resolution strategies can be turned off
* [New] Relax Javax/Jakarta rules, let all rule implementation follow a strict pattern - One rule class per capability

## Version 0.6
* [New] [#10](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/10) Rules added (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)
* [New] [#11](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/11) Rules added (Thanks [Boris Petrov](https://github.com/boris-petrov) for reporting)

## Version 0.5
* [New] [#9](https://github.com/gradlex-org/java-ecosystem-capabilities/issues/9) Rules added (Thanks [Toldry](https://github.com/Toldry) for contributing)

## Version 0.1 - 0.4
* [New] Initial releases with initial rule set
