plugins {
    id("org.gradlex.java-ecosystem-capabilities") version "0.7" // 0.6 will cause 'NoClassDefFoundError: jakarta/servlet/UnavailableException'
    id("application")
}

application {
    mainClass.set("dummy.DummyClass")
}

dependencies {
    implementation("org.apache.tomcat:tomcat-catalina:9.0.68")
    implementation("io.undertow:undertow-servlet:2.3.0.Final")
}
