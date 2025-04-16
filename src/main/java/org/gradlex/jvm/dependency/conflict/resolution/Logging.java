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

package org.gradlex.jvm.dependency.conflict.resolution;

import org.gradle.api.Action;
import org.gradle.api.artifacts.CapabilitiesResolution;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradlex.jvm.dependency.conflict.detection.rules.CapabilityDefinition;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.LoggingModuleIdentifiers;

import javax.inject.Inject;

/**
 * Project extension that enables expressing preference over potential logging capabilities conflicts.
 */
public abstract class Logging {

    @Inject
    protected abstract DependencyHandler getDependencies();

    @Inject
    protected abstract ConfigurationContainer getConfigurations();

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
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
            // Slf4j binding towards log4j2, so we need to make sure Log4j-core is selected
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_IMPL.getCapability(), validateNotation(LoggingModuleIdentifiers.LOG4J_CORE.moduleId), because);
        } else if (LoggingModuleIdentifiers.LOGBACK_CLASSIC.matches(dependency) || LoggingModuleIdentifiers.SLF4J_SIMPLE.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
            // Slf4j binding towards log4j2, so we need to make sure Log4j-core is selected
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_IMPL.getCapability(), validateNotation(LoggingModuleIdentifiers.LOG4J_CORE.moduleId), because);
        } else if (LoggingModuleIdentifiers.LOGBACK_CLASSIC.matches(dependency) || LoggingModuleIdentifiers.SLF4J_SIMPLE.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
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
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_TO_SLF4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
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
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J12API.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J12API.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_LOG4J.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_LOG4J12.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J.getCapability(), dependency, because);
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
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JUL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JUL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JUL.getCapability(), dependency, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JUL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JDK14.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JUL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JUL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JUL.getCapability(), dependency, because);
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
            selectCapabilityConflict(CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JCL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.COMMONS_LOGGING.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JCL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JCL.getCapability(), dependency, because);
            ExternalDependency commonsLogging = validateNotation(LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId);
            selectCapabilityConflict(CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), commonsLogging, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JCL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.COMMONS_LOGGING.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.SLF4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_JCL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_JCL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_VS_LOG4J2_FOR_JCL.getCapability(), dependency, because);
            ExternalDependency commonsLogging = validateNotation(LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId);
            selectCapabilityConflict(configurationName, CapabilityDefinition.COMMONS_LOGGING_IMPL.getCapability(), commonsLogging, because);
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
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
            selectCapabilityConflict(CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
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
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_IMPL.getCapability(), dependency, because);
        } else if (LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.matches(dependency) || LoggingModuleIdentifiers.LOG4J_SLF4J2_IMPL.matches(dependency)) {
            selectCapabilityConflict(configurationName, CapabilityDefinition.SLF4J_IMPL.getCapability(), dependency, because);
            selectCapabilityConflict(configurationName, CapabilityDefinition.LOG4J2_VS_SLF4J.getCapability(), dependency, because);
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
        selectSlf4JBinding(LoggingModuleIdentifiers.LOGBACK_CLASSIC.moduleId);
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
        selectSlf4JBinding(configurationName, LoggingModuleIdentifiers.LOGBACK_CLASSIC.moduleId);
        enforceSlf4JImplementation(configurationName);
    }

    /**
     * Selects {@code slf4j-simple} as the Slf4J binding and makes sure all other supported logging frameworks end up in it as well.
     * <p>
     * While having {@code slf4j-simple} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in {@code slf4j-simple} because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j-jcl} will be substituted with {@code jcl-over-slf4j}.
     */
    public void enforceSlf4JSimple() {
        selectSlf4JBinding(LoggingModuleIdentifiers.SLF4J_SIMPLE.moduleId);
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
        selectSlf4JBinding(configurationName, LoggingModuleIdentifiers.SLF4J_SIMPLE.moduleId);
        enforceSlf4JImplementation(configurationName);
    }

    /**
     * Selects {@code log4j-slf4j-impl} as the Slf4J binding and makes sure all other supported logging frameworks end up in Log4J 2 as well.
     * <p>
     * While having {@code log4j-slf4j-impl} as a dependency is required for this to work, substitution is used for enforcing other selections that could cause missed events in Log4J 2 because there are no conflicts.
     * For example, {@code commons-logging} and {@code log4j} will be configured to end up in Log4J 2 as well.
     */
    public void enforceLog4J2() {
        selectLog4J2Implementation(LoggingModuleIdentifiers.LOG4J_CORE.moduleId);
        selectSlf4JLog4J2Interaction(LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId);
        selectJulDelegation(LoggingModuleIdentifiers.LOG4J_JUL.moduleId);
        selectJCLImplementation(LoggingModuleIdentifiers.LOG4J_JCL.moduleId);
        selectLog4J12Implementation(LoggingModuleIdentifiers.LOG4J12API.moduleId);

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
        selectLog4J2Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_CORE.moduleId);
        selectSlf4JLog4J2Interaction(configurationName, LoggingModuleIdentifiers.LOG4J_SLF4J_IMPL.moduleId);
        selectJulDelegation(configurationName, LoggingModuleIdentifiers.LOG4J_JUL.moduleId);
        selectJCLImplementation(configurationName, LoggingModuleIdentifiers.LOG4J_JCL.moduleId);
        selectLog4J12Implementation(configurationName, LoggingModuleIdentifiers.LOG4J12API.moduleId);
    }

    private void enforceSlf4JImplementation() {
        selectLog4J12Implementation(LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId);
        selectJulDelegation(LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId);
        selectJCLImplementation(LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId);
        selectSlf4JLog4J2Interaction(LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId);
        selectLog4J2Implementation(LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId);

        getConfigurations().configureEach(new Slf4JEnforcementSubstitutionsUsing());
    }

    private void enforceSlf4JImplementation(String configurationName) {
        selectLog4J12Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_OVER_SLF4J.moduleId);
        selectJulDelegation(configurationName, LoggingModuleIdentifiers.JUL_TO_SLF4J.moduleId);
        selectJCLImplementation(configurationName, LoggingModuleIdentifiers.JCL_OVER_SLF4J.moduleId);
        selectSlf4JLog4J2Interaction(configurationName, LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId);
        selectLog4J2Implementation(configurationName, LoggingModuleIdentifiers.LOG4J_TO_SLF4J.moduleId);

        getConfigurations().configureEach(new Slf4JEnforcementSubstitutionsUsing(configurationName));
    }

    private ExternalDependency validateNotation(Object dependencyNotation) {
        Dependency dependency = getDependencies().create(dependencyNotation);
        if (dependency instanceof ExternalDependency) {
            return (ExternalDependency) dependency;
        } else {
            throw new IllegalArgumentException("Provided notation '" + dependencyNotation + "' cannot be converted to an ExternalDependency");
        }
    }

    private void selectCapabilityConflict(String configuration, String capabilityId, ExternalDependency target, String because) {
        getConfigurations().configureEach(conf -> {
            if (conf.getName().equals(configuration)) {
                conf.getResolutionStrategy().capabilitiesResolution(getCapabilitiesResolutionAction(capabilityId, target, because));
            }
        });
    }

    private void selectCapabilityConflict(String capabilityId, ExternalDependency target, String because) {
        getConfigurations().configureEach(conf -> conf.getResolutionStrategy().capabilitiesResolution(getCapabilitiesResolutionAction(capabilityId, target, because)));
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
