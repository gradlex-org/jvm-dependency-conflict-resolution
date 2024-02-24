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

public class Slf4J2Alignment implements ComponentMetadataRule {

    public static final String[] SLF4J2_MODULES = {
            "org.slf4j:slf4j-api",
            "org.slf4j:slf4j-simple",
            "org.slf4j:slf4j-nop",
            "org.slf4j:slf4j-jdk14",
            "org.slf4j:slf4j-jdk-platform-logging",
            "org.slf4j:slf4j-log4j12",
            "org.slf4j:slf4j-reload4j",
            "org.slf4j:slf4j-ext",
            "org.slf4j:jcl-over-slf4j",
            "org.slf4j:log4j-over-slf4j",
            "org.slf4j:jul-to-slf4j",
            "org.slf4j:osgi-over-slf4j"
    };
    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();
        String version = details.getId().getVersion();
        if (VersionNumber.parse(version).compareTo(VersionNumber.parse("2.0.8")) >= 0) {
            details.belongsTo("org.slf4j:slf4j-bom:" + version, false);
        }
    }
}
