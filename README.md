# JVM Dependency Conflict Detection and Resolution plugins

[![Build Status](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Fgradlex-org%2Fjvm-dependency-conflict-resolution%2Fbadge%3Fref%3Dmain&style=flat)](https://actions-badge.atrox.dev/gradlex-org/jvm-dependency-conflict-resolution/goto?ref=main)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin%20Portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Forg%2Fgradlex%2Fjvm-dependency-conflict-resolution%2Forg.gradlex.jvm-dependency-conflict-resolution.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/org.gradlex.jvm-dependency-conflict-resolution)

The `org.gradlex.jvm-dependency-conflict-detection` plugin adds [Capabilities](https://docs.gradle.org/current/userguide/component_capabilities.html#capabilities_as_first_level_concept) 
to the metadata of well-known components hosted on Maven Central that are used in many Java projects.

The `org.gradlex.jvm-dependency-conflict-resolution` plugin offers a compact language to address dependency conflicts by expressing strategies for Gradle to follow or to fix issues in the metadata of published components.

## What is a 'Capability' in Gradle and why should I care?

The videos below explain the concept of Capability Conflicts and why they can help you to avoid "dependency hell" in your project.
With this plugin, you enable Gradle to detect and automatically resolved typical capability conflicts in the JVM ecosystem.

[<img src="https://onepiecesoftware.github.io/img/videos/29.png" width="320">](https://www.youtube.com/watch?v=KocTqF0hO_8&list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE)
[<img src="https://onepiecesoftware.github.io/img/videos/11.png" width="320">](https://www.youtube.com/watch?v=5g20kbbqBFk&list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE)

## How to use the plugin?

See the [documentation](https://gradlex.org/jvm-dependency-conflict-resolution/).

### Supported Gradle versions

- Minimal Gradle version is `6.8.3`

Note: There are rules in this plugin (in particular for _Guava_) which work _better_ with Gradle `7+`.
This is because Gradle 7 added support for the `org.gradle.jvm.environment` attribute that allows Gradle to better distinguish between _standard-jvm_ and _android_ projects.

### Notes on the Plugins history

These plugins join, unify and extend functionalities that were previously part of the following discontinued plugins:

- https://plugins.gradle.org/plugin/org.gradlex.java-ecosystem-capabilities
- https://plugins.gradle.org/plugin/dev.jacomet.logging-capabilities
- https://plugins.gradle.org/plugin/de.jjohannes.java-ecosystem-capabilities
- https://plugins.gradle.org/plugin/de.jjohannes.missing-metadata-guava

**NOTE on Guava**:
Some of these plugins are used to patch older versions of Guava.
This functionality is discontinued.
Most of the patching is not included anymore by default, because Guava now publishes richer metadata and some of the adjustments were opinionated.
If you use the latest Guava version, you may not need anything specific.
If some of the transitive dependencies are not wanted in your case, you can fine tune them using the `jvmDependencyConflicts.patch` DSL. E.g.:

```kotlin
jvmDependencyConflicts {
    patch {
        module("com.google.guava:guava") {
            removeDependency("com.google.code.findbugs:jsr305")
            reduceToCompileOnlyApiDependency("com.google.errorprone:error_prone_annotations")
        }
    }
}
```

If you need the previous behavior for older Guava versions, you can explicitly apply the `GuavaComponentRule`.

```kotlin
dependencies.components {
    withModule<GuavaComponentRule>("com.google.guava:guava")
}
```

## Disclaimer

Gradle and the Gradle logo are trademarks of Gradle, Inc.
The GradleX project is not endorsed by, affiliated with, or associated with Gradle or Gradle, Inc. in any way.
