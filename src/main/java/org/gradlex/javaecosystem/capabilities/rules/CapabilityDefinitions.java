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

import java.util.Arrays;
import java.util.List;

public enum CapabilityDefinitions {

    ASM("asm", "asm",
            "org.ow2.asm:asm"
    ),
    JAKARTA_MAIL_API("jakarta.mail", "jakarta.mail-api", JakartaMailApiRule.class,
            "com.sun.mail:mailapi",
            "com.sun.mail:jakarta.mail",
            "org.eclipse.angus:jakarta.mail"
    ),
    SERVLET_API("javax.servlet", "servlet-api", JavaxServletApiRule.class,
            "javax.servlet:javax.servlet-api",
            "jakarta.servlet:jakarta.servlet-api",
            "org.apache.tomcat:servlet-api",
            "org.apache.tomcat:tomcat-servlet-api",
            "org.apache.tomcat.embed:tomcat-embed-core",
            "servletapi:servletapi"
    );

    public String group;
    public String name;
    public List<String> modules;
    public Class<? extends ComponentMetadataRule> ruleClass;

    CapabilityDefinitions(String group, String name, String... modules) {
        this.group = group;
        this.name = name;
        this.modules = Arrays.asList(modules);
        this.ruleClass = EnumBasedRule.class;
    }

    CapabilityDefinitions(String group, String name, Class<? extends ComponentMetadataRule> ruleClass, String... modules) {
        this.group = group;
        this.name = name;
        this.modules = Arrays.asList(modules);
        this.ruleClass = ruleClass;
    }

    public String getCapability() {
        return group + ":" + name;
    }
}
