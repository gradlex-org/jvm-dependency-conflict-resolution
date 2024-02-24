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

package org.gradlex.javaecosystem.capabilities;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.util.GradleVersion;
import org.gradlex.javaecosystem.capabilities.componentrules.GuavaComponentRule;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2Alignment;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4J2Alignment;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JAlignment;

public class JavaEcosystemCapabilitiesBasePlugin implements Plugin<ExtensionAware> {

    static final String PLUGIN_ID = "org.gradlex.java-ecosystem-capabilities-base";

    // Minimal version that works reliably with alignment and has the substitution rules `using` API and has rulesMode
    // setting in dependencyResolutionManagement
    private static final GradleVersion MINIMUM_SUPPORTED_VERSION = GradleVersion.version("6.8.3");

    @Override
    public void apply(ExtensionAware projectOrSettings) {
        if (GradleVersion.current().compareTo(MINIMUM_SUPPORTED_VERSION) < 0) {
            throw new IllegalStateException("Plugin requires at least Gradle " + MINIMUM_SUPPORTED_VERSION.getVersion());
        }

        ComponentMetadataHandler components;
        if (projectOrSettings instanceof Project) {
            // Make sure 'jvm-ecosystem' is applied which adds the schemas for the attributes this plugin relies on
            ((Project) projectOrSettings).getPlugins().apply("jvm-ecosystem");
            components = ((Project) projectOrSettings).getDependencies().getComponents();
        } else if (projectOrSettings instanceof Settings) {
            //noinspection UnstableApiUsage
            components = ((Settings) projectOrSettings).getDependencyResolutionManagement().getComponents();
        } else {
            throw new IllegalStateException("Cannot apply plugin to: " + projectOrSettings.getClass().getName());
        }
        registerCapabilityRules(components);
    }


    private void registerCapabilityRules(ComponentMetadataHandler components) {
        for (CapabilityDefinitions rule : CapabilityDefinitions.values()) {
            registerRuleFromEnum(rule, components);
        }

        configureAlignment(components); // TODO alignment

        registerComponentRules(components); // TODO move out here
    }

    private void registerRuleFromEnum(CapabilityDefinitions capabilityDefinitions, ComponentMetadataHandler components) {
        for (String module : capabilityDefinitions.modules) {
            components.withModule(module, capabilityDefinitions.ruleClass, ac -> ac.params(capabilityDefinitions));
        }
    }

    private void registerComponentRules(ComponentMetadataHandler components) {
        components.withModule(GuavaComponentRule.MODULE, GuavaComponentRule.class);
    }

    private void configureAlignment(ComponentMetadataHandler components) {
        for (String slf4jModule : Slf4JAlignment.SLF4J_MODULES) {
            components.withModule(slf4jModule, Slf4JAlignment.class);
        }
        for (String slf4j2Module : Slf4J2Alignment.SLF4J2_MODULES) {
            components.withModule(slf4j2Module, Slf4J2Alignment.class);
        }
        for (String log4j2Module : Log4J2Alignment.LOG4J2_MODULES) {
            components.withModule(log4j2Module, Log4J2Alignment.class);
        }
    }
}
