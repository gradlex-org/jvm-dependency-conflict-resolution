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

package org.gradlex.javaecosystem.conflict.detection.rules.logging;

import org.gradle.api.artifacts.Dependency;

public enum LoggingModuleIdentifiers {
    LOG4J_SLF4J_IMPL("org.apache.logging.log4j", "log4j-slf4j-impl", "2.0"),
    LOG4J_SLF4J2_IMPL("org.apache.logging.log4j", "log4j-slf4j2-impl", "2.19.0"),
    LOG4J_TO_SLF4J("org.apache.logging.log4j", "log4j-to-slf4j", "2.0"),
    SLF4J_SIMPLE("org.slf4j", "slf4j-simple", "1.0"),
    LOGBACK_CLASSIC("ch.qos.logback", "logback-classic", "1.0.0"),
    SLF4J_LOG4J12("org.slf4j", "slf4j-log4j12", "1.0"),
    SLF4J_JCL("org.slf4j", "slf4j-jcl", "1.0"),
    SLF4J_JDK14("org.slf4j", "slf4j-jdk14", "1.0"),
    LOG4J_OVER_SLF4J("org.slf4j", "log4j-over-slf4j", "1.4.2"),
    LOG4J12API("org.apache.logging.log4j", "log4j-1.2-api", "2.0"),
    LOG4J("log4j", "log4j", "1.1.3"),
    JUL_TO_SLF4J("org.slf4j", "jul-to-slf4j", "1.5.10"),
    LOG4J_JUL("org.apache.logging.log4j", "log4j-jul", "2.1"),
    COMMONS_LOGGING("commons-logging", "commons-logging", "1.0"),
    JCL_OVER_SLF4J("org.slf4j", "jcl-over-slf4j", "1.5.10"),
    LOG4J_JCL("org.apache.logging.log4j", "log4j-jcl", "2.0"),
    LOG4J_CORE("org.apache.logging.log4j", "log4j-core", "2.0"),
    SPRING_JCL("org.springframework", "spring-jcl", "5.0.0.RELEASE");

    public final String moduleId;
    public final String group;
    public final String name;
    private final String firstVersion;

    LoggingModuleIdentifiers(String group, String name, String firstVersion) {
        this.group = group;
        this.name = name;
        this.firstVersion = firstVersion;
        this.moduleId = group + ":" + name;
    }

    public boolean matches(Dependency dependency) {
        return group.equals(dependency.getGroup()) && name.equals(dependency.getName());
    }

    public String asFirstVersion() {
        return moduleId + ":" + firstVersion;
    }
}
