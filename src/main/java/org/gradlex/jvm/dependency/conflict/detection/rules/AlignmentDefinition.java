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

package org.gradlex.jvm.dependency.conflict.detection.rules;

import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Log4J2Alignment;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Slf4J2Alignment;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Slf4JAlignment;

import java.util.Arrays;
import java.util.List;

public enum AlignmentDefinition {
    LOG4J2("org.apache.logging.log4j:log4j-bom", Log4J2Alignment.class,
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
    ),
    SLF4J2("org.slf4j:slf4j-bom", Slf4J2Alignment.class,
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
    ),
    SLF4J(Slf4JAlignment.class,
            // Ignored modules:
            //   org.slf4j:slf4j-archetype
            //   org.slf4j:slf4j-converter
            //   org.slf4j:slf4j-log4j13
            //   org.slf4j:slf4j-site
            //   org.slf4j:slf4j-skin<
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
    );

    private final String bom;
    private final List<String> modules;
    private final Class<? extends AlignmentDefinitionRule> ruleClass;

    AlignmentDefinition(Class<? extends AlignmentDefinitionRule> ruleClass, String... modules) {
        this.bom = null;
        this.ruleClass = ruleClass;
        this.modules = Arrays.asList(modules);
    }

    AlignmentDefinition(String bom, Class<? extends AlignmentDefinitionRule> ruleClass, String... modules) {
        this.bom = bom;
        this.ruleClass = ruleClass;
        this.modules = Arrays.asList(modules);
    }

    public String getBom() {
        return bom;
    }

    public List<String> getModules() {
        return modules;
    }

    public Class<? extends AlignmentDefinitionRule> getRuleClass() {
        return ruleClass;
    }

    public boolean hasBom() {
        return bom != null;
    }
}
