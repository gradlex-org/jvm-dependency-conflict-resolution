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

import org.gradlex.jvm.dependency.conflict.detection.rules.asm.AsmAlignmentRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.jetty.JettyAlignmentRule;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Log4J2Alignment;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Slf4J2Alignment;
import org.gradlex.jvm.dependency.conflict.detection.rules.logging.Slf4JAlignment;

import java.util.Arrays;
import java.util.List;

public enum AlignmentDefinition {
    ASM("org.ow2.asm:asm-bom", AsmAlignmentRule.class,
            "org.ow2.asm:asm",
            "org.ow2.asm:asm-tree",
            "org.ow2.asm:asm-analysis",
            "org.ow2.asm:asm-util",
            "org.ow2.asm:asm-commons"
    ),
    JERSEY("org.eclipse.jetty:jetty-bom", AlignmentDefinitionRule.class,
            "org.glassfish.jersey.core:jersey-common",
            "org.glassfish.jersey.core:jersey-client",
            "org.glassfish.jersey.core:jersey-server",
            "org.glassfish.jersey.bundles:jaxrs-ri",
            "org.glassfish.jersey.connectors:jersey-apache-connector",
            "org.glassfish.jersey.connectors:jersey-apache5-connector",
            "org.glassfish.jersey.connectors:jersey-helidon-connector",
            "org.glassfish.jersey.connectors:jersey-grizzly-connector",
            "org.glassfish.jersey.connectors:jersey-jnh-connector",
            "org.glassfish.jersey.connectors:jersey-jetty-connector",
            "org.glassfish.jersey.connectors:jersey-jetty11-connector",
            "org.glassfish.jersey.connectors:jersey-jetty-http2-connector",
            "org.glassfish.jersey.connectors:jersey-jdk-connector",
            "org.glassfish.jersey.connectors:jersey-netty-connector",
            "org.glassfish.jersey.containers:jersey-container-jetty-http",
            "org.glassfish.jersey.containers:jersey-container-jetty11-http",
            "org.glassfish.jersey.containers:jersey-container-jetty-http2",
            "org.glassfish.jersey.containers:jersey-container-grizzly2-http",
            "org.glassfish.jersey.containers:jersey-container-grizzly2-servlet",
            "org.glassfish.jersey.containers:jersey-container-jetty-servlet",
            "org.glassfish.jersey.containers:jersey-container-jdk-http",
            "org.glassfish.jersey.containers:jersey-container-netty-http",
            "org.glassfish.jersey.containers:jersey-container-servlet",
            "org.glassfish.jersey.containers:jersey-container-servlet-core",
            "org.glassfish.jersey.containers:jersey-container-simple-http",
            "org.glassfish.jersey.containers.glassfish:jersey-gf-ejb",
            "org.glassfish.jersey.ext:jersey-bean-validation",
            "org.glassfish.jersey.ext:jersey-entity-filtering",
            "org.glassfish.jersey.ext:jersey-micrometer",
            "org.glassfish.jersey.ext:jersey-metainf-services",
            "org.glassfish.jersey.ext.microprofile:jersey-mp-config",
            "org.glassfish.jersey.ext:jersey-mvc",
            "org.glassfish.jersey.ext:jersey-mvc-bean-validation",
            "org.glassfish.jersey.ext:jersey-mvc-freemarker",
            "org.glassfish.jersey.ext:jersey-mvc-jsp",
            "org.glassfish.jersey.ext:jersey-mvc-mustache",
            "org.glassfish.jersey.ext:jersey-proxy-client",
            "org.glassfish.jersey.ext:jersey-spring6",
            "org.glassfish.jersey.ext:jersey-declarative-linking",
            "org.glassfish.jersey.ext:jersey-wadl-doclet",
            "org.glassfish.jersey.ext.cdi:jersey-weld2-se",
            "org.glassfish.jersey.ext.cdi:jersey-cdi1x",
            "org.glassfish.jersey.ext.cdi:jersey-cdi1x-transaction",
            "org.glassfish.jersey.ext.cdi:jersey-cdi1x-validation",
            "org.glassfish.jersey.ext.cdi:jersey-cdi1x-servlet",
            "org.glassfish.jersey.ext.cdi:jersey-cdi1x-ban-custom-hk2-binding",
            "org.glassfish.jersey.ext.cdi:jersey-cdi-rs-inject",
            "org.glassfish.jersey.ext.rx:jersey-rx-client-guava",
            "org.glassfish.jersey.ext.rx:jersey-rx-client-rxjava",
            "org.glassfish.jersey.ext.rx:jersey-rx-client-rxjava2",
            "org.glassfish.jersey.ext.microprofile:jersey-mp-rest-client",
            "org.glassfish.jersey.media:jersey-media-jaxb",
            "org.glassfish.jersey.media:jersey-media-json-jackson",
            "org.glassfish.jersey.media:jersey-media-json-jettison",
            "org.glassfish.jersey.media:jersey-media-json-processing",
            "org.glassfish.jersey.media:jersey-media-json-gson",
            "org.glassfish.jersey.media:jersey-media-json-binding",
            "org.glassfish.jersey.media:jersey-media-kryo",
            "org.glassfish.jersey.media:jersey-media-moxy",
            "org.glassfish.jersey.media:jersey-media-multipart",
            "org.glassfish.jersey.media:jersey-media-sse",
            "org.glassfish.jersey.security:oauth1-client",
            "org.glassfish.jersey.security:oauth1-server",
            "org.glassfish.jersey.security:oauth1-signature",
            "org.glassfish.jersey.security:oauth2-client",
            "org.glassfish.jersey.inject:jersey-hk2",
            "org.glassfish.jersey.inject:jersey-cdi2-se",
            "org.glassfish.jersey.test-framework:jersey-test-framework-core",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-bundle",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-external",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-grizzly2",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-inmemory",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-jdk-http",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-simple",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-jetty",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-jetty-http2",
            "org.glassfish.jersey.test-framework.providers:jersey-test-framework-provider-netty",
            "org.glassfish.jersey.test-framework:jersey-test-framework-util"
    ),
    JETTY("org.eclipse.jetty:jetty-bom", JettyAlignmentRule.class,
            "org.eclipse.jetty:jetty-alpn-client",
            "org.eclipse.jetty:jetty-alpn-conscrypt-client",
            "org.eclipse.jetty:jetty-alpn-conscrypt-server",
            "org.eclipse.jetty:jetty-alpn-java-client",
            "org.eclipse.jetty:jetty-alpn-java-server",
            "org.eclipse.jetty:jetty-alpn-server",
            "org.eclipse.jetty:jetty-client",
            "org.eclipse.jetty:jetty-deploy",
            "org.eclipse.jetty:jetty-http",
            "org.eclipse.jetty:jetty-http-spi",
            "org.eclipse.jetty:jetty-http-tools",
            "org.eclipse.jetty:jetty-io",
            "org.eclipse.jetty:jetty-jmx",
            "org.eclipse.jetty:jetty-jndi",
            "org.eclipse.jetty:jetty-keystore",
            "org.eclipse.jetty:jetty-openid",
            "org.eclipse.jetty:jetty-osgi",
            "org.eclipse.jetty:jetty-plus",
            "org.eclipse.jetty:jetty-proxy",
            "org.eclipse.jetty:jetty-rewrite",
            "org.eclipse.jetty:jetty-security",
            "org.eclipse.jetty:jetty-server",
            "org.eclipse.jetty:jetty-session",
            "org.eclipse.jetty:jetty-slf4j-impl",
            "org.eclipse.jetty:jetty-start",
            "org.eclipse.jetty:jetty-unixdomain-server",
            "org.eclipse.jetty:jetty-util",
            "org.eclipse.jetty:jetty-util-ajax",
            "org.eclipse.jetty:jetty-xml",
            "org.eclipse.jetty.demos:jetty-demo-handler",
            "org.eclipse.jetty.fcgi:jetty-fcgi-client",
            "org.eclipse.jetty.fcgi:jetty-fcgi-proxy",
            "org.eclipse.jetty.fcgi:jetty-fcgi-server",
            "org.eclipse.jetty.http2:jetty-http2-client",
            "org.eclipse.jetty.http2:jetty-http2-client-transport",
            "org.eclipse.jetty.http2:jetty-http2-common",
            "org.eclipse.jetty.http2:jetty-http2-hpack",
            "org.eclipse.jetty.http2:jetty-http2-server",
            "org.eclipse.jetty.http3:jetty-http3-client",
            "org.eclipse.jetty.http3:jetty-http3-client-transport",
            "org.eclipse.jetty.http3:jetty-http3-common",
            "org.eclipse.jetty.http3:jetty-http3-qpack",
            "org.eclipse.jetty.http3:jetty-http3-server",
            "org.eclipse.jetty.quic:jetty-quic-client",
            "org.eclipse.jetty.quic:jetty-quic-common",
            "org.eclipse.jetty.quic:jetty-quic-quiche-common",
            "org.eclipse.jetty.quic:jetty-quic-quiche-foreign",
            "org.eclipse.jetty.quic:jetty-quic-quiche-jna",
            "org.eclipse.jetty.quic:jetty-quic-server",
            "org.eclipse.jetty.websocket:jetty-websocket-core-client",
            "org.eclipse.jetty.websocket:jetty-websocket-core-common",
            "org.eclipse.jetty.websocket:jetty-websocket-core-server",
            "org.eclipse.jetty.websocket:jetty-websocket-jetty-api",
            "org.eclipse.jetty.websocket:jetty-websocket-jetty-client",
            "org.eclipse.jetty.websocket:jetty-websocket-jetty-common",
            "org.eclipse.jetty.websocket:jetty-websocket-jetty-server"
    ),
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
    SSHD(AlignmentDefinitionRule.class,
            "org.apache.sshd:sshd-cli",
            "org.apache.sshd:sshd-common",
            "org.apache.sshd:sshd-contrib",
            "org.apache.sshd:sshd-core",
            "org.apache.sshd:sshd-git",
            "org.apache.sshd:sshd-ldap",
            "org.apache.sshd:sshd-mina",
            "org.apache.sshd:sshd-netty",
            "org.apache.sshd:sshd-openpgp",
            "org.apache.sshd:sshd-osgi",
            "org.apache.sshd:sshd-pam",
            "org.apache.sshd:sshd-putty",
            "org.apache.sshd:sshd-scp",
            "org.apache.sshd:sshd-sftp",
            "org.apache.sshd:sshd-spring-sftp"
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
