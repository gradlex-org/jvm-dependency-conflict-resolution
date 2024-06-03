import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition

plugins {
    id("org.gradlex.jvm-dependency-conflict-resolution")
    id("java-library")
}

dependencies {
    implementation("org.apache.sshd:sshd-common:2.8.0")
    implementation("org.apache.sshd:sshd-core:2.9.2")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-tree:9.3")
    implementation("org.eclipse.jetty:jetty-server:11.0.13")
    implementation("org.eclipse.jetty:jetty-servlet:11.0.0")
    implementation("org.eclipse.jetty.toolchain:jetty-test-helper:6.0")
    implementation("org.glassfish.jersey.core:jersey-common:3.1.0")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.0.6")
}

jvmDependencyConflicts {
    conflictResolution {
        select(CapabilityDefinition.JAKARTA_ACTIVATION_API, "com.sun.activation:jakarta.activation")
    }
}
