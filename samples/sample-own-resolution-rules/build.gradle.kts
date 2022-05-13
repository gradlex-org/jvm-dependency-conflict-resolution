plugins {
    id("de.jjohannes.java-ecosystem-capabilities")
    id("java-library")
}

configurations.all {
    resolutionStrategy.capabilitiesResolution {
        withCapability("cglib:cglib") {
            select("cglib:cglib:0")
        }
    }
    resolutionStrategy.capabilitiesResolution {
        withCapability("javax.mail:mail") {
           select("com.sun.mail:jakarta.mail:0")
        }
    }
    resolutionStrategy.capabilitiesResolution {
        withCapability("javax.ws.rs:jsr311-api") {
            select("org.jboss.resteasy:jaxrs-api:0")
        }
    }
    resolutionStrategy.capabilitiesResolution {
        withCapability("javax.servlet:servlet-api") {
            select("jakarta.servlet:jakarta.servlet-api:0")
        }
    }
}

dependencies {
    implementation("cglib:cglib-nodep:3.2.10")
    implementation("cglib:cglib:3.2.10")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("com.sun.mail:mailapi:2.0.1")
    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation("org.apache.tomcat:tomcat-servlet-api:10.0.18")
    implementation("org.jboss.resteasy:jaxrs-api:3.0.0.Final")
    implementation("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:2.0.2.Final")
    implementation("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_3.0_spec:1.0.1.Final")
}