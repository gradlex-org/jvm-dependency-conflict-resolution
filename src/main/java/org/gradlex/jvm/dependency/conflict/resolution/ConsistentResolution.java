// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import static org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin.INTERNAL_CONFIGURATION_NAME;
import static org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin.MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME;

import java.util.Collections;
import javax.inject.Inject;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.attributes.Usage;
import org.gradle.api.attributes.java.TargetJvmEnvironment;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.util.GradleVersion;

public abstract class ConsistentResolution {

    private final SourceSetContainer sourceSets;

    @Inject
    public ConsistentResolution(SourceSetContainer sourceSets) {
        this.sourceSets = sourceSets;
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract ConfigurationContainer getConfigurations();

    @Inject
    protected abstract DependencyHandler getDependencies();

    /**
     * The runtime classpath of the given project always respected in version conflict detection and resolution.
     */
    public NamedDomainObjectProvider<Configuration> providesVersions(String versionProvidingProject) {
        NamedDomainObjectProvider<Configuration> mainRuntimeClasspath = maybeCreateMainRuntimeClasspathConfiguration();
        getDependencies().add(mainRuntimeClasspath.getName(), createDependency(versionProvidingProject));
        return mainRuntimeClasspath;
    }

    /**
     * A platform/BOM (<a href="https://docs.gradle.org/current/userguide/java_platform_plugin.html">Java Platform Plugin</a>)
     * used to provide versions not available through consistent resolution alone.
     * Useful if additional dependencies are needed only for tests.
     */
    public void platform(String platform) {
        NamedDomainObjectProvider<Configuration> internal = maybeCreateInternalConfiguration();
        internal.configure(conf -> conf.withDependencies(d -> {
            Dependency platformDependency = getDependencies().platform(createDependency(platform));
            d.add(platformDependency);
        }));

        sourceSets.configureEach(sourceSet -> {
            ConfigurationContainer configurations = getConfigurations();
            configurations.named(sourceSet.getRuntimeClasspathConfigurationName(), c -> c.extendsFrom(internal.get()));
            configurations.named(sourceSet.getCompileClasspathConfigurationName(), c -> c.extendsFrom(internal.get()));
            configurations.named(
                    sourceSet.getAnnotationProcessorConfigurationName(), c -> c.extendsFrom(internal.get()));
        });
    }

    private NamedDomainObjectProvider<Configuration> maybeCreateMainRuntimeClasspathConfiguration() {
        if (getConfigurations().getNames().contains(MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME)) {
            return getConfigurations().named(MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
        }

        NamedDomainObjectProvider<Configuration> internal = maybeCreateInternalConfiguration();
        NamedDomainObjectProvider<Configuration> mainRuntimeClasspath = getConfigurations()
                .register(MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME, c -> {
                    ObjectFactory objects = getObjects();
                    c.setCanBeResolved(true);
                    c.setCanBeConsumed(false);
                    c.extendsFrom(internal.get());
                    c.getAttributes().attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME));
                    c.getAttributes()
                            .attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY));
                    c.getAttributes()
                            .attribute(
                                    LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                                    objects.named(LibraryElements.class, LibraryElements.JAR));
                    c.getAttributes()
                            .attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL));
                    if (GradleVersion.current().compareTo(GradleVersion.version("7.0")) >= 0) {
                        c.getAttributes()
                                .attribute(
                                        TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                                        objects.named(TargetJvmEnvironment.class, TargetJvmEnvironment.STANDARD_JVM));
                    }
                });
        sourceSets.configureEach(sourceSet -> {
            ConfigurationContainer configurations = getConfigurations();
            NamedDomainObjectProvider<Configuration> runtime = configurations.named(
                    sourceSet.getRuntimeClasspathConfigurationName(),
                    c -> c.shouldResolveConsistentlyWith(mainRuntimeClasspath.get()));
            configurations.named(
                    sourceSet.getCompileClasspathConfigurationName(),
                    c -> c.shouldResolveConsistentlyWith(runtime.get()));
            configurations.named(
                    sourceSet.getAnnotationProcessorConfigurationName(),
                    c -> c.shouldResolveConsistentlyWith(runtime.get()));
        });
        return mainRuntimeClasspath;
    }

    private NamedDomainObjectProvider<Configuration> maybeCreateInternalConfiguration() {
        if (getConfigurations().getNames().contains(INTERNAL_CONFIGURATION_NAME)) {
            return getConfigurations().named(INTERNAL_CONFIGURATION_NAME);
        }
        return getConfigurations().register(INTERNAL_CONFIGURATION_NAME, c -> {
            c.setCanBeResolved(false);
            c.setCanBeConsumed(false);
        });
    }

    private Dependency createDependency(String project) {
        boolean isProjectInBuild = project.startsWith(":");
        return getDependencies()
                .create(
                        isProjectInBuild
                                ? getDependencies().project(Collections.singletonMap("path", project))
                                : project);
    }
}
