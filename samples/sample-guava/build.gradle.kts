plugins {
    id("org.gradlex.java-ecosystem-capabilities") version "0.7"
    id("de.jjohannes.missing-metadata-guava") version "31.1.1"
    id("application")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

application {
    mainClass.set("dummy.DummyClass")
}

dependencies {
    implementation("com.google.guava:guava:31.1-android")
}

tasks.register("printClasspath") {
    doLast {
        configurations.compileClasspath.get().forEach { println(it.name) } // will give 'guava-31.1-jre.jar'
    }
}
