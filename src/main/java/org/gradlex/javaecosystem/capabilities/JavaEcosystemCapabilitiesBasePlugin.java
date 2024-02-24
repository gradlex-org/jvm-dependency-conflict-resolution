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
import org.gradlex.javaecosystem.capabilities.rules.AopallianceRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcmailRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcpgRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcpkixRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcprovRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBctlsRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBctspRule;
import org.gradlex.javaecosystem.capabilities.rules.BouncycastleBcutilRule;
import org.gradlex.javaecosystem.capabilities.rules.C3p0Rule;
import org.gradlex.javaecosystem.capabilities.rules.CGlibRule;
import org.gradlex.javaecosystem.capabilities.rules.CapabilityDefinitions;
import org.gradlex.javaecosystem.capabilities.rules.CommonsIoRule;
import org.gradlex.javaecosystem.capabilities.rules.Dom4jRule;
import org.gradlex.javaecosystem.capabilities.rules.FindbugsAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.GoogleCollectionsRule;
import org.gradlex.javaecosystem.capabilities.rules.GuavaListenableFutureRule;
import org.gradlex.javaecosystem.capabilities.rules.GuavaRule;
import org.gradlex.javaecosystem.capabilities.rules.HikariCPRule;
import org.gradlex.javaecosystem.capabilities.rules.IntellijAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaActivationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaActivationImplementationRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaAnnotationApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaJsonApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaServletApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWebsocketClientApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JakartaWsRsApiRule;
import org.gradlex.javaecosystem.capabilities.rules.JavaAssistRule;
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
import org.gradlex.javaecosystem.capabilities.rules.JcipAnnotationsRule;
import org.gradlex.javaecosystem.capabilities.rules.JnaPlatformRule;
import org.gradlex.javaecosystem.capabilities.rules.JtsCoreRule;
import org.gradlex.javaecosystem.capabilities.rules.JtsRule;
import org.gradlex.javaecosystem.capabilities.rules.JunitRule;
import org.gradlex.javaecosystem.capabilities.rules.PostgresqlRule;
import org.gradlex.javaecosystem.capabilities.rules.StaxApiRule;
import org.gradlex.javaecosystem.capabilities.rules.VelocityRule;
import org.gradlex.javaecosystem.capabilities.rules.WoodstoxAslRule;
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
        registerRule(AopallianceRule.MODULES, AopallianceRule.class, components);
        registerRule(BouncycastleBcmailRule.MODULES, BouncycastleBcmailRule.class, components);
        registerRule(BouncycastleBcpgRule.MODULES, BouncycastleBcpgRule.class, components);
        registerRule(BouncycastleBcpkixRule.MODULES, BouncycastleBcpkixRule.class, components);
        registerRule(BouncycastleBcprovRule.MODULES, BouncycastleBcprovRule.class, components);
        registerRule(BouncycastleBctlsRule.MODULES, BouncycastleBctlsRule.class, components);
        registerRule(BouncycastleBctspRule.MODULES, BouncycastleBctspRule.class, components);
        registerRule(BouncycastleBcutilRule.MODULES, BouncycastleBcutilRule.class, components);
        registerRule(C3p0Rule.MODULES, C3p0Rule.class, components);
        registerRule(CGlibRule.MODULES, CGlibRule.class, components);
        registerRule(CommonsIoRule.MODULES, CommonsIoRule.class, components);
        registerRule(Dom4jRule.MODULES, Dom4jRule.class, components);
        registerRule(FindbugsAnnotationsRule.MODULES, FindbugsAnnotationsRule.class, components);
        registerRule(GoogleCollectionsRule.MODULES, GoogleCollectionsRule.class, components);
        registerRule(GuavaListenableFutureRule.MODULES, GuavaListenableFutureRule.class, components);
        registerRule(GuavaRule.MODULES, GuavaRule.class, components);
        registerRule(HikariCPRule.MODULES, HikariCPRule.class, components);
        registerRule(IntellijAnnotationsRule.MODULES, IntellijAnnotationsRule.class, components);

        registerRule(JakartaActivationApiRule.MODULES, JakartaActivationApiRule.class, components);
        registerRule(JakartaActivationImplementationRule.MODULES, JakartaActivationImplementationRule.class, components);
        registerRule(JakartaAnnotationApiRule.MODULES, JakartaAnnotationApiRule.class, components);
        registerRule(JakartaJsonApiRule.MODULES, JakartaJsonApiRule.class, components);
        registerRule(JakartaServletApiRule.MODULES, JakartaServletApiRule.class, components);
        registerRule(JakartaWebsocketApiRule.MODULES, JakartaWebsocketApiRule.class, components);
        registerRule(JakartaWebsocketClientApiRule.MODULES, JakartaWebsocketClientApiRule.class, components);
        registerRule(JakartaWsRsApiRule.MODULES, JakartaWsRsApiRule.class, components);

        registerRule(JavaAssistRule.MODULES, JavaAssistRule.class, components);

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

        registerRule(JcipAnnotationsRule.MODULES, JcipAnnotationsRule.class, components);
        registerRule(JnaPlatformRule.MODULES, JnaPlatformRule.class, components);
        registerRule(JtsCoreRule.MODULES, JtsCoreRule.class, components);
        registerRule(JtsRule.MODULES, JtsRule.class, components);
        registerRule(JunitRule.MODULES, JunitRule.class, components);
        registerRule(PostgresqlRule.MODULES, PostgresqlRule.class, components);
        registerRule(StaxApiRule.MODULES, StaxApiRule.class, components);
        registerRule(VelocityRule.MODULES, VelocityRule.class, components);
        registerRule(WoodstoxAslRule.MODULES, WoodstoxAslRule.class, components);

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
