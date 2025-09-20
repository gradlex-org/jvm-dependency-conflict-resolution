package org.gradlex.jvm.dependency.conflict.resolution;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.component.ComponentSelector;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.LoggingModuleIdentifiers;

public class Log4J2EnforcementSubstitutionsUsing implements Action<Configuration> {
    @Override
    public void execute(Configuration configuration) {
        configuration.getResolutionStrategy().dependencySubstitution(substitution -> {
            // TODO We are missing substitutions here

            // TODO this is wrong, it cannot be a substitution for commons-logging. It must be a dependency addition
            ComponentSelector jclOverLog4J = substitution.module(LoggingModuleIdentifiers.LOG4J_JCL.asFirstVersion());
            substitution.substitute(substitution.module(LoggingModuleIdentifiers.COMMONS_LOGGING.moduleId)).using(jclOverLog4J);
            substitution.substitute(substitution.module(LoggingModuleIdentifiers.LOG4J_JCL.moduleId)).using(jclOverLog4J);
            substitution.substitute(substitution.module(LoggingModuleIdentifiers.SPRING_JCL.moduleId)).using(jclOverLog4J);

        });
    }
}
