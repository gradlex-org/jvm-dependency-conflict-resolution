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

package org.gradlex.javaecosystem.capabilities.rules.logging;

import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataDetails;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

public class Slf4JAlignment implements ComponentMetadataRule {

    /**
     * The list of SLF4J modules that should be aligned to the same version as the slf4j-api.
     * <p>
     * Ignored modules:
     * <ul>
     *     <li>org.slf4j:slf4j-archetype</li>
     *     <li>org.slf4j:slf4j-converter</li>
     *     <li>org.slf4j:slf4j-log4j13</li>
     *     <li>org.slf4j:slf4j-site</li>
     *     <li>org.slf4j:slf4j-skin</li>
     * </ul>
     */
    public static final String[] SLF4J_MODULES = {
            "org.slf4j:integration",
            "org.slf4j:jcl-over-slf4j",
            "org.slf4j:jcl104-over-slf4j",
            "org.slf4j:jul-to-slf4j",
            "org.slf4j:log4j-over-slf4j",
            "org.slf4j:nlog4j",
            "org.slf4j:osgi-over-slf4j",
            "org.slf4j:slf4j-android",
            "org.slf4j:slf4j-api",
            "org.slf4j:slf4j-archetype",
            "org.slf4j:slf4j-converter",
            "org.slf4j:slf4j-ext",
            "org.slf4j:slf4j-jcl",
            "org.slf4j:slf4j-jdk-platform-logging",
            "org.slf4j:slf4j-jdk14",
            "org.slf4j:slf4j-log4j12",
            "org.slf4j:slf4j-migrator",
            "org.slf4j:slf4j-nop",
            "org.slf4j:slf4j-reload4j",
            "org.slf4j:slf4j-simple"
    };
    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        String version = details.getId().getVersion();
        if (VersionNumber.parse(version).compareTo(VersionNumber.parse("2.0.8")) < 0) {
            details.belongsTo("org.gradlex.logging.align:slf4j:" + version);
        }
    }
}
