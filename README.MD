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

# Disclaimer

Gradle and the Gradle logo are trademarks of Gradle, Inc.
The GradleX project is not endorsed by, affiliated with, or associated with Gradle or Gradle, Inc. in any way.
