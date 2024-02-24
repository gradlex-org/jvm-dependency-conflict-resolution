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
import org.gradlex.javaecosystem.capabilities.rules.JakartaServletApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketClientApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWsRsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxActivationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxAnnotationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxEjbApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxElApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxInjectApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxJsonApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxJwsApisRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxMailApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxPersistenceApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxServletJspRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxServletJstlRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxSoapApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxValidationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxWebsocketApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxWsRsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxXmlBindApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaxXmlWsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2Alignment;
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

        registerRule(JavaxActivationApiRule.MODULES, JavaxActivationApiRule.class, components);
        registerRule(JavaxAnnotationApiRule.MODULES, JavaxAnnotationApiRule.class, components);
        registerRule(JavaxEjbApiRule.MODULES, JavaxEjbApiRule.class, components);
        registerRule(JavaxElApiRule.MODULES, JavaxElApiRule.class, components);
        registerRule(JavaxInjectApiRule.MODULES, JavaxInjectApiRule.class, components);
        registerRule(JavaxJsonApiRule.MODULES, JavaxJsonApiRule.class, components);
        registerRule(JavaxJwsApisRule.MODULES, JavaxJwsApisRule.class, components);
        registerRule(JavaxMailApiRule.MODULES, JavaxMailApiRule.class, components);
        registerRule(JavaxPersistenceApiRule.MODULES, JavaxPersistenceApiRule.class, components);
        registerRule(JavaxServletJspRule.MODULES, JavaxServletJspRule.class, components);
        registerRule(JavaxServletJstlRule.MODULES, JavaxServletJstlRule.class, components);
        registerRule(JavaxSoapApiRule.MODULES, JavaxSoapApiRule.class, components);
        registerRule(JavaxValidationApiRule.MODULES, JavaxValidationApiRule.class, components);
        registerRule(JavaxWebsocketApiRule.MODULES, JavaxWebsocketApiRule.class, components);
        registerRule(JavaxWsRsApiRule.MODULES, JavaxWsRsApiRule.class, components);
        registerRule(JavaxXmlBindApiRule.MODULES, JavaxXmlBindApiRule.class, components);
        registerRule(JavaxXmlWsApiRule.MODULES, JavaxXmlWsApiRule.class, components);

        // logging
        configureAlignment(components);

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

    private void registerRule(
            String[] modules,
            Class<? extends ComponentMetadataRule> rule,
            ComponentMetadataHandler components) {


        for (String module : modules) {
            components.withModule(module, rule);
        }
    }

    private void configureAlignment(ComponentMetadataHandler components) {
        components.all(Slf4JAlignment.class);
        components.all(Log4J2Alignment.class);
    }
}
