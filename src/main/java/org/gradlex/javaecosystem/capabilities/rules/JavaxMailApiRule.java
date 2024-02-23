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
public abstract class JavaxMailApiRule implements ComponentMetadataRule {

    public static final String CAPABILITY_GROUP = "javax.mail";
    public static final String CAPABILITY_NAME = "mail";
    public static final String CAPABILITY = CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public static final String FIRST_JAKARTA_VERSION = "2.0.0";

    public static final String[] MODULES = {
            // API only
            "com.sun.mail:mailapi",
            "jakarta.mail:jakarta.mail-api",
            "javax.mail:javax.mail-api",
            // API + Implementation
            "com.sun.mail:javax.mail",
            "com.sun.mail:jakarta.mail",
            // Apache Geronimo
            "org.apache.geronimo.javamail:geronimo-javamail_1.3.1_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.3.1_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.3.1_spec",
            "org.apache.geronimo.javamail:geronimo-javamail_1.4_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.4_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.4_spec",
            "org.apache.geronimo.javamail:geronimo-javamail_1.6_mail",
            "org.apache.geronimo.javamail:geronimo-javamail_1.6_provider",
            "org.apache.geronimo.specs:geronimo-javamail_1.6_spec"
    };

    @Override
    public void execute(ComponentMetadataContext context) {
        String name = context.getDetails().getId().getName();
        String group = context.getDetails().getId().getGroup();
        String version;

        if (group.equals("org.apache.geronimo.javamail") || group.equals("org.apache.geronimo.specs")) {
            version = mailApiVersionForGeronimoName(name);
        } else {
            version = context.getDetails().getId().getVersion();
        }

        if (VersionNumber.parse(version).compareTo(VersionNumber.parse(FIRST_JAKARTA_VERSION)) < 0) {
            context.getDetails().allVariants(variant -> variant.withCapabilities(capabilities ->
                    capabilities.addCapability(CAPABILITY_GROUP, CAPABILITY_NAME, version)
            ));
        }
    }

    private String mailApiVersionForGeronimoName(String name) {
        int index = "geronimo-javamail_".length();
        return name.substring(index, index + 3) + ".0";
    }
}
