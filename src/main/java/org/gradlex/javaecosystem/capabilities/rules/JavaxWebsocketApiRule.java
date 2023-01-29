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

package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

@CacheableRule
@NonNullApi
public abstract class JavaxWebsocketApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.websocket";
    public static final String CAPABILITY_NAME = "javax.websocket-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String FIRST_JAKARTA_VERSION = "2.0.0";

    public static final String[] MODULES = {
            "jakarta.websocket:jakarta.websocket-api",
            // in javax namespace, websocket-api and websocket-client-api overlap
            "javax.websocket:javax.websocket-client-api",
            "jakarta.websocket:jakarta.websocket-client-api",
            "org.apache.tomcat:tomcat-websocket-api",
            "org.apache.tomcat:tomcat-websocket",
            "org.apache.tomcat.embed:tomcat-embed-websocket",
            "org.eclipse.jetty.toolchain:jetty-javax-websocket-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String group = context.getDetails().getId().getGroup();
        String version;
        if (group.startsWith("org.apache.tomcat")) {
            version = websocketApiVersionForTomcatVersion(VersionNumber.parse(context.getDetails().getId().getVersion()));
        } else {
            version = context.getDetails().getId().getVersion();
        }

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }

    // https://tomcat.apache.org/whichversion.html
    static String websocketApiVersionForTomcatVersion(VersionNumber tomcatVersion) {
        if (tomcatVersion.compareTo(VersionNumber.version(10, 1)) >= 0) {
            return "2.1.0";
        }
        if (tomcatVersion.compareTo(VersionNumber.version(10, 0)) >= 0) {
            return "2.0.0";
        }
        return "1.1.0";
    }
}
