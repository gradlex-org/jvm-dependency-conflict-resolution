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

/**
 * Adds capability {@code dev.jacomet.logging:log4j2-vs-slf4j:<version>} to all variants, using the version of the module targeted.
 */
public class Log4J2vsSlf4J extends VersionedCapabilityRule {
    public static final String CAPABILITY_NAME = "log4j2-vs-slf4j";
    public static final String CAPABILITY_ID = FixedCapabilityRule.CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public Log4J2vsSlf4J() {
        super(CAPABILITY_NAME);
    }

}
