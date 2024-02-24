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

public class Log4J2Alignment implements ComponentMetadataRule {

    public static String[] LOG4J2_MODULES = {
            "org.apache.logging.log4j:log4j-api",
            "org.apache.logging.log4j:log4j-core",
            "org.apache.logging.log4j:log4j-1.2-api",
            "org.apache.logging.log4j:log4j-jcl",
            "org.apache.logging.log4j:log4j-flume-ng",
            "org.apache.logging.log4j:log4j-taglib",
            "org.apache.logging.log4j:log4j-jmx-gui",
            "org.apache.logging.log4j:log4j-slf4j-impl",
            "org.apache.logging.log4j:log4j-web",
            "org.apache.logging.log4j:log4j-nosql"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        ComponentMetadataDetails details = context.getDetails();

        String version = details.getId().getVersion();
        if (VersionNumber.parse(version).compareTo(VersionNumber.parse("2.0")) >= 0) {
            details.belongsTo("org.apache.logging.log4j:log4j-bom:" + version, false);
        }
    }
}
