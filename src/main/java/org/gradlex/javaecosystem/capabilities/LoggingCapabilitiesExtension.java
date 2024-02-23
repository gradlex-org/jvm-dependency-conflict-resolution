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

import org.gradle.api.Action;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradlex.javaecosystem.capabilities.actions.Slf4JEnforcementSubstitutionsUsing;
import org.gradlex.javaecosystem.capabilities.rules.logging.CommonsLoggingImplementationRule;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2Implementation;
import org.gradlex.javaecosystem.capabilities.rules.logging.Log4J2vsSlf4J;
import org.gradlex.javaecosystem.capabilities.rules.logging.LoggingModuleIdentifiers;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JImplementation;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JVsJCL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JVsLog4J2ForJCL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsJUL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J2ForJUL;
import org.gradlex.javaecosystem.capabilities.rules.logging.Slf4JvsLog4J2ForLog4J;

/**
 * Project extension that enables expressing preference over potential logging capabilities conflicts.
 */
public class LoggingCapabilitiesExtension {
    private final ConfigurationContainer configurations;
    private final DependencyHandler dependencies;

    public LoggingCapabilitiesExtension(ConfigurationContainer configurations, DependencyHandler dependencies) {
        this.configurations = configurations;
        this.dependencies = dependencies;
    }

    /**
     * Selects the provided module as the Slf4J binding to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the Slf4J binding module as a dependency or {@code group:name:version} notation
     */
    public void selectSlf4JBinding(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Slf4J binding";
        if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsJUL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(Slf4JVsJCL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
            // Slf4j binding towards log4j2, so we need to make sure Log4j-core is selected
            selectCapabilityConflict(Log4J2Implementation.CAPABILITY_ID, validateNotation(LoggingModuleIdentifiers.LOG4J_CORE.moduleId), because);
        } else if (LoggingModuleIdentifiers.LOGBACK_CLASSIC.matches(dependency) || LoggingModuleIdentifiers.SLF4J_SIMPLE.matches(dependency)) {
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Slf4J binding");
        }
    }

    /**
     * Selects the provided module as the Slf4J binding to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the Slf4J binding module as a dependency or {@code group:name:version} notation
     */
    public void selectSlf4JBinding(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Slf4J binding";
        if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsJUL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JVsJCL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
            // Slf4j binding towards log4j2, so we need to make sure Log4j-core is selected
            selectCapabilityConflict(configurationName, Log4J2Implementation.CAPABILITY_ID, validateNotation(LoggingModuleIdentifiers.LOG4J_CORE.moduleId), because);
        } else if (LoggingModuleIdentifiers.LOGBACK_CLASSIC.matches(dependency) || LoggingModuleIdentifiers.SLF4J_SIMPLE.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Slf4J binding");
        }
    }

    /**
     * Selects the provided module as the Log4J2 implementation to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the Log4J 2 implementation as a dependency or {@code group:name:version} notation
     */
    public void selectLog4J2Implementation(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Log4J2 implementation";
        if (LoggingModuleIdentifiers.LOG4J_CORE.matches(dependency)) {
            selectCapabilityConflict(Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Log4J2 implementation");
        }
    }

    /**
     * Selects the provided module as the Log4J2 implementation to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the Log4J 2 implementation as a dependency or {@code group:name:version} notation
     */
    public void selectLog4J2Implementation(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Log4J2 implementation";
        if (LoggingModuleIdentifiers.LOG4J_CORE.matches(dependency)) {
            selectCapabilityConflict(configurationName, Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Log4J2 implementation");
        }
    }

    /**
     * Selects the provided module as the Log4J 1.2 implementation to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the Log4J 1.2 implementation module as a dependency or {@code group:name:version} notation
     */
    public void selectLog4J12Implementation(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Log4J implementation";
        if (LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J12API.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Log4J implementation");
        }
    }

    /**
     * Selects the provided module as the Log4J 1.2 implementation to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the Log4J 1.2 implementation module as a dependency or {@code group:name:version} notation
     */
    public void selectLog4J12Implementation(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Log4J implementation";
        if (LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J12API.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J2ForLog4J.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Log4J implementation");
        }
    }

    /**
     * Selects the provided module as the java util logging delegation to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the JUL delegation module as a dependency or {@code group:name:version} notation
     */
    public void selectJulDelegation(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected JUL delegation";
        if (LoggingModuleIdentifiers.JUL_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J2ForJUL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JvsJUL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsJUL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JUL.matches(dependency)) {
            selectCapabilityConflict(Slf4JvsLog4J2ForJUL.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid JUL delegation");
        }
    }

    /**
     * Selects the provided module as the java util logging delegation to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the JUL delegation module as a dependency or {@code group:name:version} notation
     */
    public void selectJulDelegation(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected JUL delegation";
        if (LoggingModuleIdentifiers.JUL_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J2ForJUL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JvsJUL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsJUL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JUL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JvsLog4J2ForJUL.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid JUL delegation");
        }
    }

    /**
     * Selects the provided module as the commons-logging implementation to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the commons-logging implementation module as a dependency or {@code group:name:version} notation
     */
    public void selectJCLImplementation(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected JCL implementation";
        if (LoggingModuleIdentifiers.JCL_OVER_SLF4J.matches(dependency)) {
            selectCapabilityConflict(CommonsLoggingImplementationRule.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JVsJCL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Slf4JVsLog4J2ForJCL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.COMMONS_LOGGING.matches(dependency)) {
            selectCapabilityConflict(CommonsLoggingImplementationRule.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(Slf4JVsJCL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JCL.matches(dependency)) {
            selectCapabilityConflict(Slf4JVsLog4J2ForJCL.CAPABILITY_ID, dependency, because);
            ExternalDependency commonsLogging = validateNotation(LoggingModuleIdentifiers.COMMONS_LOGGING.asVersionZero());
            selectCapabilityConflict(CommonsLoggingImplementationRule.CAPABILITY_ID, commonsLogging, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid JCL implementation");
        }
    }

    /**
     * Selects the provided module as the commons-logging implementation to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the commons-logging implementation module as a dependency or {@code group:name:version} notation
     */
    public void selectJCLImplementation(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected JCL implementation";
        if (LoggingModuleIdentifiers.JCL_OVER_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, CommonsLoggingImplementationRule.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JVsJCL.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Slf4JVsLog4J2ForJCL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.COMMONS_LOGGING.matches(dependency)) {
            selectCapabilityConflict(configurationName, CommonsLoggingImplementationRule.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JVsJCL.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JVsLog4J2ForJCL.CAPABILITY_ID, dependency, because);
            ExternalDependency commonsLogging = validateNotation(LoggingModuleIdentifiers.COMMONS_LOGGING.asVersionZero());
            selectCapabilityConflict(configurationName, CommonsLoggingImplementationRule.CAPABILITY_ID, commonsLogging, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid JCL implementation");
        }
    }

    /**
     * Selects the provided module as the Slf4J / Log4J 2 interaction to use.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param dependencyNotation the Slf4J / Log4J 2 interaction module as a dependency or {@code group:name:version} notation
     */
    public void selectSlf4JLog4J2Interaction(Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Slf4J Log4J 2 interaction";
        if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(Slf4JImplementation.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Slf4J / Log4J 2 interaction");
        }
    }

    /**
     * Selects the provided module as the Slf4J / Log4J 2 interaction to use for the resolution of the given configuration.
     * <p>
     * This also resolves all other potential conflicts with the passed in module in favor of it.
     *
     * @param configurationName  the configuration to be setup
     * @param dependencyNotation the Slf4J / Log4J 2 interaction module as a dependency or {@code group:name:version} notation
     */
    public void selectSlf4JLog4J2Interaction(String configurationName, Object dependencyNotation) {
        ExternalDependency dependency = validateNotation(dependencyNotation);
        String because = "Logging capabilities plugin selected Slf4J Log4J 2 interaction";
        if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Log4J2Implementation.CAPABILITY_ID, dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(configurationName, Slf4JImplementation.CAPABILITY_ID, dependency, because);
            selectCapabilityConflict(configurationName, Log4J2vsSlf4J.CAPABILITY_ID, dependency, because);
        } else {
            throw new IllegalArgumentException("Provided dependency '" + dependency + "' is not a valid Slf4J / Log4J 2 interaction");
        }
    }

    /**
     * Selects logback as the Slf4J binding and makes sure all other supported logging frameworks end up in logback as well.
     * <p>
     * While having logback as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in logback because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j-jcl} will be substituted with {@code jcl-over-slf4j}.
     */
    public void enforceLogback() {
        selectSlf4JBinding(LoggingModuleIdentifiers.LOGBACK_CLASSIC.asVersionZero());
        enforceSlf4JImplementation();
    }

    /**
     * Selects logback as the Slf4J binding and makes sure all other supported logging frameworks end up in logback as well for the resolution of the given configuration.
     * <p>
     * While having logback as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in logback because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j-jcl} will be substituted with {@code jcl-over-slf4j}.
     *
     * @param configurationName the configuration to be setup
     */
    public void enforceLogback(String configurationName) {
        selectSlf4JBinding(configurationName, LoggingModuleIdentifiers.LOGBACK_CLASSIC.asVersionZero());
        enforceSlf4JImplementation(configurationName);
    }

    /**
     * Selects {@code slf4j-simple} as the Slf4J binding and makes sure all other supported logging frameworks end up in it as well.
     * <p>
     * While having {@code slf4j-simple} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in {@code slf4j-simple} because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j-jcl} will be substituted with {@code jcl-over-slf4j}.
     */
    public void enforceSlf4JSimple() {
        selectSlf4JBinding(LoggingModuleIdentifiers.SLF4J_SIMPLE.asVersionZero());
        enforceSlf4JImplementation();
    }

    /**
     * Selects {@code slf4j-simple} as the Slf4J binding and makes sure all other supported logging frameworks end up in it as well for the resolution of the given configuration.
     * <p>
     * While having {@code slf4j-simple} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in {@code slf4j-simple} because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j-jcl} will be substituted with {@code jcl-over-slf4j}.
     *
     * @param configurationName the configuration to be setup
     */
    public void enforceSlf4JSimple(String configurationName) {
        selectSlf4JBinding(configurationName, LoggingModuleIdentifiers.SLF4J_SIMPLE.asVersionZero());
        enforceSlf4JImplementation(configurationName);
    }

    /**
     * Selects {@code log4j-slf4j-impl} as the Slf4J binding and makes sure all other supported logging frameworks end up in Log4J 2 as well.
     * <p>
     * While having {@code log4j-slf4j-impl} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in Log4J 2 because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j} will be configured to end up in Log4J 2 as well.
     */
    public void enforceLog4J2() {
        selectLog4J2Implementation(LoggingModuleIdentifiers.LOG4J_CORE.asVersionZero());
        selectSlf4JLog4J2Interaction(LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.asVersionZero());
        selectJulDelegation(LoggingModuleIdentifiers.LOG4J_JUL.asVersionZero());
        selectJCLImplementation(LoggingModuleIdentifiers.LOG4J_JCL.asVersionZero());
        selectLog4J12Implementation(LoggingModuleIdentifiers.LOG4J12API.asVersionZero());

    }

    /**
     * Selects {@code log4j-slf4j-impl} as the Slf4J binding and makes sure all other supported logging frameworks end up in Log4J 2 as well for the resolution of the given configuration.
     * <p>
     * While having {@code log4j-slf4j-impl} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in Log4J 2 because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j} will be configured to end up in Log4J 2 as well.
     *
     * @param configurationName the configuration to be setup
     */
    public void enforceLog4J2(String configurationName) {
        selectLog4J2Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_CORE.asVersionZero());
        selectSlf4JLog4J2Interaction(configurationName, LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.asVersionZero());
        selectJulDelegation(configurationName, LoggingModuleIdentifiers.LOG4J_JUL.asVersionZero());
        selectJCLImplementation(configurationName, LoggingModuleIdentifiers.LOG4J_JCL.asVersionZero());
        selectLog4J12Implementation(configurationName, LoggingModuleIdentifiers.LOG4J12API.asVersionZero());
    }

    private void enforceSlf4JImplementation() {
        selectLog4J12Implementation(LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.asVersionZero());
        selectJulDelegation(LoggingModuleIdentifiers.JUL_TO_SLF4J.asVersionZero());
        selectJCLImplementation(LoggingModuleIdentifiers.JCL_OVER_SLF4J.asVersionZero());
        selectSlf4JLog4J2Interaction(LoggingModuleIdentifiers.LOG4J_TO_SLF4J.asVersionZero());
        selectLog4J2Implementation(LoggingModuleIdentifiers.LOG4J_TO_SLF4J.asVersionZero());

        configurations.all(getSlf4JEnforcementSubstitutions());
    }

    private void enforceSlf4JImplementation(String configurationName) {
        selectLog4J12Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.asVersionZero());
        selectJulDelegation(configurationName, LoggingModuleIdentifiers.JUL_TO_SLF4J.asVersionZero());
        selectJCLImplementation(configurationName, LoggingModuleIdentifiers.JCL_OVER_SLF4J.asVersionZero());
        selectSlf4JLog4J2Interaction(configurationName, LoggingModuleIdentifiers.LOG4J_TO_SLF4J.asVersionZero());
        selectLog4J2Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_TO_SLF4J.asVersionZero());

        configurations.matching(conf -> conf.getName().equals(configurationName)).all(getSlf4JEnforcementSubstitutions());
    }

    private Action<Configuration> getSlf4JEnforcementSubstitutions() {
        return new Slf4JEnforcementSubstitutionsUsing();
    }

    private ExternalDependency validateNotation(Object dependencyNotation) {
        Dependency dependency = dependencies.create(dependencyNotation);
        if (dependency instanceof ExternalDependency) {
            return (ExternalDependency) dependency;
        } else {
            throw new IllegalArgumentException("Provided notation '" + dependencyNotation + "' cannot be converted to an ExternalDependency");
        }
    }

    private void selectCapabilityConflict(String configuration, String capabilityId, ExternalDependency target, String because) {
        configurations.matching(conf -> conf.getName().equals(configuration)).all(conf -> conf.getResolutionStrategy().capabilitiesResolution(getCapabilitiesResolutionAction(capabilityId, target, because)));
    }

    private void selectCapabilityConflict(String capabilityId, ExternalDependency target, String because) {
        configurations.all(conf -> conf.getResolutionStrategy().capabilitiesResolution(getCapabilitiesResolutionAction(capabilityId, target, because)));
    }

    private Action<CapabilitiesResolution> getCapabilitiesResolutionAction(String capabilityId, ExternalDependency target, String because) {
        return resolution -> resolution.withCapability(capabilityId, details -> {
            details.getCandidates().stream().filter(candidate -> {
                ComponentIdentifier id = candidate.getId();
                if (!(id instanceof ModuleComponentIdentifier)) {
                    return false;
                }
                ModuleComponentIdentifier moduleId = (ModuleComponentIdentifier) id;
                return moduleId.getGroup().equals(target.getGroup())
                        && moduleId.getModule().equals(target.getName());
            }).findFirst().ifPresent(candidate -> details.select(candidate).because(because));
        });
    }

}
