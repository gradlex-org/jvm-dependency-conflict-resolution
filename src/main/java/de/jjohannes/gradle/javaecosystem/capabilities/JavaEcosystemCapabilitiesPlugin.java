/*
 * Copyright 2022 the GradleX team.
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

package de.jjohannes.gradle.javaecosystem.capabilities;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.PluginAware;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
@NonNullApi
public abstract class JavaEcosystemCapabilitiesPlugin implements Plugin<PluginAware> {

    @Override
    public void apply(PluginAware projectOrSettings) {
        Logger logger = (Logger) LoggerFactory.getLogger(JavaEcosystemCapabilitiesPlugin.class);
        logger.lifecycle("Plugin ID changed - please update your build:\n" +
                "  Plugin ID: de.jjohannes.java-ecosystem-capabilities -> org.gradlex.java-ecosystem-capabilities\n" +
                "  GA Coordinates: de.jjohannes.gradle:java-ecosystem-capabilities -> org.gradlex:java-ecosystem-capabilities");
        projectOrSettings.getPlugins().apply("org.gradlex.java-ecosystem-capabilities");
    }

}
