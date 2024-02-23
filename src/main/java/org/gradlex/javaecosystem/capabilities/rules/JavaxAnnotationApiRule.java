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

package org.gradlex.javaecosystem.capabilities.rules;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.CacheableRule;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.ComponentMetadataRule;
import org.gradlex.javaecosystem.capabilities.util.VersionNumber;

@CacheableRule
@NonNullApi
public abstract class JavaxAnnotationApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.annotation";
    public static final String CAPABILITY_NAME = "jsr250-api";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String FIRST_JAKARTA_VERSION = "2.0.0";

    public static final String[] MODULES = {
            "jakarta.annotation:jakarta.annotation-api",
            "javax.annotation:javax.annotation-api",
            "org.apache.tomcat:tomcat-annotations-api"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String version;
        if ("org.apache.tomcat".equals(context.getDetails().getId().getGroup())) {
            version = annotationApiVersionForTomcatVersion(VersionNumber.parse(context.getDetails().getId().getVersion()));
        } else {
            version = context.getDetails().getId().getVersion();
        }

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }

    // This is probably 100% accurate - older Tomcat versions might ship older 1.x specs
    private static String annotationApiVersionForTomcatVersion(VersionNumber tomcatVersion) {
        if (tomcatVersion.compareTo(VersionNumber.version(10, 0)) >= 0) {
            return "2.1.0";
        }
        return "1.3.0";
    }
}
