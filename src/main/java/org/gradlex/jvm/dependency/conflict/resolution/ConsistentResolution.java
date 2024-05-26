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

import javax.inject.Inject;
import java.util.Collections;

import static org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin.INTERNAL_CONFIGURATION_NAME;
import static org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictResolutionPlugin.MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME;

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
    public Configuration providesVersions(String versionProvidingProject) {
        Configuration mainRuntimeClasspath = maybeCreateMainRuntimeClasspathConfiguration();
        getDependencies().add(mainRuntimeClasspath.getName(), createDependency(versionProvidingProject));
        return mainRuntimeClasspath;
    }

    /**
     * A platform/BOM (<a href="https://docs.gradle.org/current/userguide/java_platform_plugin.html">Java Platform Plugin</a>)
     * used to provide versions not available through consistent resolution alone.
     * Useful if additional dependencies are needed only for tests.
     */
    public void platform(String platform) {
        Configuration internal = maybeCreateInternalConfiguration();
        internal.withDependencies(d -> {
            Dependency platformDependency = getDependencies().platform(createDependency(platform));
            d.add(platformDependency);
        });

        sourceSets.configureEach(sourceSet -> {
            ConfigurationContainer configurations = getConfigurations();
            configurations.getByName(sourceSet.getRuntimeClasspathConfigurationName()).extendsFrom(internal);
            configurations.getByName(sourceSet.getCompileClasspathConfigurationName()).extendsFrom(internal);
            configurations.getByName(sourceSet.getAnnotationProcessorConfigurationName()).extendsFrom(internal);
        });
    }

    private Configuration maybeCreateMainRuntimeClasspathConfiguration() {
        Configuration existing = getConfigurations().findByName(MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME);
        if (existing != null) {
            return existing;
        }

        Configuration mainRuntimeClasspath = getConfigurations().create(MAIN_RUNTIME_CLASSPATH_CONFIGURATION_NAME, c -> {
            ObjectFactory objects = getObjects();
            c.setCanBeResolved(true);
            c.setCanBeConsumed(false);
            c.extendsFrom(maybeCreateInternalConfiguration());
            c.getAttributes().attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME));
            c.getAttributes().attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY));
            c.getAttributes().attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.JAR));
            c.getAttributes().attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL));
            if (GradleVersion.current().compareTo(GradleVersion.version("7.0")) >= 0) {
                c.getAttributes().attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                        objects.named(TargetJvmEnvironment.class, TargetJvmEnvironment.STANDARD_JVM));
            }
        });
        sourceSets.configureEach(sourceSet -> {
            ConfigurationContainer configurations = getConfigurations();
            Configuration runtime = configurations.getByName(sourceSet.getRuntimeClasspathConfigurationName());
            Configuration compile = configurations.getByName(sourceSet.getCompileClasspathConfigurationName());
            Configuration processor = configurations.getByName(sourceSet.getAnnotationProcessorConfigurationName());
            runtime.shouldResolveConsistentlyWith(mainRuntimeClasspath);
            compile.shouldResolveConsistentlyWith(runtime);
            processor.shouldResolveConsistentlyWith(runtime);
        });
        return mainRuntimeClasspath;
    }

    private Configuration maybeCreateInternalConfiguration() {
        Configuration internal = getConfigurations().findByName(INTERNAL_CONFIGURATION_NAME);
        if (internal != null) {
            return internal;
        }
        return getConfigurations().create(INTERNAL_CONFIGURATION_NAME, c -> {
            c.setCanBeResolved(false);
            c.setCanBeConsumed(false);
        });
    }

    private Dependency createDependency(String project) {
        boolean isProjectInBuild = project.startsWith(":");
        return getDependencies().create(isProjectInBuild
                ? getDependencies().project(Collections.singletonMap("path", project))
                : project);
    }
}
