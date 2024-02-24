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

package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy;
import org.gradlex.javaecosystem.capabilities.rules.logging.LoggingModuleIdentifiers;

import java.util.Arrays;
import java.util.List;

import static org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy.FIRST_MODULE;
import static org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy.HIGHEST_VERSION;
import static org.gradlex.javaecosystem.capabilities.resolution.DefaultResolutionStrategy.NONE;

public enum CapabilityDefinitions {

    ASM(HIGHEST_VERSION,
        "asm:asm",
        "org.ow2.asm:asm"
    ),

    HAMCREST_LIBRARY(FIRST_MODULE,
        "org.hamcrest:hamcrest",
        "org.hamcrest:hamcrest-library"
    ),
    HAMCREST_CORE(FIRST_MODULE,
        "org.hamcrest:hamcrest",
        "org.hamcrest:hamcrest-core"
    ),

    JAKARTA_MAIL_API(NONE, JakartaMailApiRule.class,
        "jakarta.mail:jakarta.mail-api",
        "com.sun.mail:mailapi",
        "com.sun.mail:jakarta.mail",
        "org.eclipse.angus:jakarta.mail"
    ),
    JAVAX_SERVLET_API(NONE, JavaxServletApiRule.class,
        "javax.servlet:servlet-api",
        "javax.servlet:javax.servlet-api",
        "jakarta.servlet:jakarta.servlet-api",
        "org.apache.tomcat:servlet-api",
        "org.apache.tomcat:tomcat-servlet-api",
        "org.apache.tomcat.embed:tomcat-embed-core",
        "servletapi:servletapi"
    ),
    /**
     * Log4J2 has its own implementation with `log4j-core`.
     * It can also delegate to Slf4J with `log4j-to-slf4j`.
     * <p>
     * Given the above:
     * * `log4j-core` and `log4j-to-slf4j` are exclusive
     */
    LOG4J2_IMPL(NONE,
        LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId,
        LoggingModuleIdentifiers.LOG4J_CORE.moduleId
    ),

    /**
     * `commons-logging:commons-logging` can be replaced by:
     * * Slf4J with `org.slf4j:jcl-over-slf4j`
     * * Log4J2 with `org.apache.logging.log4j:log4j-jcl` _which requires `commons-logging`_
     * * Spring JCL with `org.springframework:spring-jcl`
     * <p>
     * `commons-logging:commons-logging` can be used from:
     * * Slf4J API delegating to it with `org.slf4j:slf4j-jcl`
     * * Log4J2 API only through Slf4J delegation
     * <p>
     * Given the above:
     * * `jcl-over-slf4j` and `slf4j-jcl` are exclusive
     * * `commons-logging`, `jcl-over-slf4j` and `spring-jcl` are exclusive
     * * `jcl-over-slf4j` and `log4j-jcl` are exclusive
     */
    COMMONS_LOGGING_IMPL(NONE, FixedCapabilityRule.class,
            LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SPRING_JCL.moduleId
    ),
    SLF4J_VS_JCL(NONE,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_JCL.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_JCL(NONE, FixedCapabilityRule.class,
            LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J_JCL.moduleId
    ),
    /**
     * Log4J2 can act as an Slf4J implementation with `log4j-slf4j-impl` or `log4j-slf4j2-impl`.
     * It can also delegate to Slf4J with `log4j-to-slf4j`.
     * <p>
     * Given the above:
     * * `log4j-slf4j-impl`, `log4j-slf4j2-impl` and `log4j-to-slf4j` are exclusive
     */
    LOG4J2_VS_SLF4J(NONE,
            LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId
    ),
    /**
     * Slf4J provides an API, which requires an implementation.
     * Only one implementation can be on the classpath, selected between:
     * * `slf4j-simple`
     * * `logback-classic`
     * * `slf4j-log4j12` to use Log4J 1.2
     * * `slf4j-jcl` to use Jakarta Commons Logging
     * * `slf4j-jdk14` to use Java Util Logging
     * * `log4j-slf4j-impl` to use Log4J2
     */
    SLF4J_IMPL(NONE, FixedCapabilityRule.class,
            LoggingModuleIdentifiers.SLF4J_SIMPLE.moduleId,
            LoggingModuleIdentifiers.LOGBACK_CLASSIC.moduleId,
            LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId,
            LoggingModuleIdentifiers.SLF4J_JCL.moduleId,
            LoggingModuleIdentifiers.SLF4J_JDK14.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId,
            LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId
    ),
    /**
     * `log4j:log4j` can be replaced by:
     * * Slf4j with `log4j-over-slf4j`
     * * Log4J2 with `log4j-1.2-api`
     * <p>
     * Log4J can be used from:
     * * Slf4J API delegating to it with `slf4j-log4j12`
     * * Log4J2 API only through Slf4J delegation
     * <p>
     * Given the above:
     * * `log4j-over-slf4j` and `slf4j-log4j12` are exclusive
     * * `log4j-over-slf4j` and `log4j-1.2-api` and `log4j` are exclusive
     */
    SLF4J_VS_LOG4J(NONE,
            LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_LOG4J(NONE, FixedCapabilityRule.class,
            LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J12API.moduleId,
            LoggingModuleIdentifiers.LOG4J.moduleId
    ),
    /**
     * Java Util Logging can be replaced by:
     * * Slf4J with `jul-to-slf4j`
     * * Log4J2 with `log4j-jul`
     * <p>
     * Java Util Logging can be used from:
     * * Slf4J API delegating to it with `slf4j-jdk14`
     * * Log4J2 API only through SLF4J delegation
     * <p>
     * Given the above:
     * * `jul-to-slf4j` and `slf4j-jdk14` are exclusive
     * * `jul-to-slf4j` and `log4j-jul` are exclusive
     */
    SLF4J_VS_JUL(NONE,
            LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId,
            LoggingModuleIdentifiers.SLF4J_JDK14.moduleId
    ),
    SLF4J_VS_LOG4J2_FOR_JUL(NONE, FixedCapabilityRule.class,
            LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId,
            LoggingModuleIdentifiers.LOG4J_JUL.moduleId
    );

    public final String group;
    public final String name;
    public final List<String> modules;
    public final DefaultResolutionStrategy defaultStrategy;
    public final Class<? extends ComponentMetadataRule> ruleClass;

    CapabilityDefinitions(DefaultResolutionStrategy strategy, String... modules) {
        this(strategy, EnumBasedRule.class, modules);
    }

    CapabilityDefinitions(DefaultResolutionStrategy defaultStrategy, Class<? extends ComponentMetadataRule> ruleClass, String... modules) {
        this.group = "org.gradlex";
        this.name = nameInKebabCase();
        this.modules = Arrays.asList(modules);
        this.defaultStrategy = defaultStrategy;
        this.ruleClass = ruleClass;
    }

    private String nameInKebabCase() {
        return name().toLowerCase().replace("_", "-");
    }

    public String getCapability() {
        return group + ":" + name;
    }
}
