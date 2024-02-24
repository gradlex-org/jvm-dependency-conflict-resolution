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
    ;

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
