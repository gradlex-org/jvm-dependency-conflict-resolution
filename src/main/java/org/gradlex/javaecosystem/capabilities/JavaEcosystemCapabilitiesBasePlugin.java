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

import org.gradle.api.NonNullApi;
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
import org.gradlex.javaecosystem.capabilities.rules.HamcrestCoreRule;
import org.gradlex.javaecosystem.capabilities.rules.HamcrestLibraryRule;
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
import org.gradlex.javaecosystem.capabilities.rules.logging.CommonsLoggingImplementationRule;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2Alignment;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2vsSlf4J;
import org.gradlex.javaecosystem.capabilities.rules.logging.LoggingModuleIdentifiers;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JAlignment;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JImplementation;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JVsJCL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JVsLog4J2ForJCL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsJUL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J2ForJUL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J2ForLog4J;

@NonNullApi
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
        registerRule(HamcrestCoreRule.MODULES, HamcrestCoreRule.class, components);
        registerRule(HamcrestLibraryRule.MODULES, HamcrestLibraryRule.class, components);
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
        configureCommonsLogging(components);
        configureJavaUtilLogging(components);
        configureLog4J(components);
        configureSlf4J(components);
        configureLog4J2(components);

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


    /**
     * Log4J2 can act as an Slf4J implementation with `log4j-slf4j-impl` or `log4j-slf4j2-impl`.
     * It can also delegate to Slf4J with `log4j-to-slf4j`.
     * <p>
     * Given the above:
     * * `log4j-slf4j-impl`, `log4j-slf4j2-impl` and `log4j-to-slf4j` are exclusive
     */
    private void configureLog4J2(ComponentMetadataHandler components) {
        components.withModule(LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId, Log4J2vsSlf4J.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId, Log4J2vsSlf4J.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId, Log4J2vsSlf4J.class);
    }

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
    private void configureSlf4J(ComponentMetadataHandler components) {
        components.withModule(LoggingModuleIdentifiers.SLF4J_SIMPLE.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.LOGBACK_CLASSIC.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_JCL.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_JDK14.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId, Slf4JImplementation.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.moduleId, Slf4JImplementation.class);
    }

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
    private void configureLog4J(ComponentMetadataHandler components) {
        components.withModule(LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId, Slf4JvsLog4J.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_LOG4J12.moduleId, Slf4JvsLog4J.class);

        components.withModule(LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId, Slf4JvsLog4J2ForLog4J.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J12API.moduleId, Slf4JvsLog4J2ForLog4J.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J.moduleId, Slf4JvsLog4J2ForLog4J.class);
    }

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
    private void configureJavaUtilLogging(ComponentMetadataHandler components) {
        components.withModule(LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId, Slf4JvsJUL.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_JDK14.moduleId, Slf4JvsJUL.class);

        components.withModule(LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId, Slf4JvsLog4J2ForJUL.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_JUL.moduleId, Slf4JvsLog4J2ForJUL.class);
    }

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
    private void configureCommonsLogging(ComponentMetadataHandler components) {
        components.withModule(LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId, CommonsLoggingImplementationRule.class);
        components.withModule(LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId, CommonsLoggingImplementationRule.class);
        components.withModule(LoggingModuleIdentifiers.SPRING_JCL.moduleId, CommonsLoggingImplementationRule.class);

        components.withModule(LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId, Slf4JVsJCL.class);
        components.withModule(LoggingModuleIdentifiers.SLF4J_JCL.moduleId, Slf4JVsJCL.class);

        components.withModule(LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId, Slf4JVsLog4J2ForJCL.class);
        components.withModule(LoggingModuleIdentifiers.LOG4J_JCL.moduleId, Slf4JVsLog4J2ForJCL.class);
    }

    private void configureAlignment(ComponentMetadataHandler components) {
        components.all(Slf4JAlignment.class);
        components.all(Log4J2Alignment.class);
    }
}
