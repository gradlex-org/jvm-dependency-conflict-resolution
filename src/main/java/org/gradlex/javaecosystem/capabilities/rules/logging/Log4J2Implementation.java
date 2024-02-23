package org.gradlex.javaecosystem.capabilities.rules.logging;

public class Log4J2Implementation extends VersionedCapabilityRule {

    public static final String CAPABILITY_NAME = "log4j2-impl";
    public static final String CAPABILITY_ID = FixedCapabilityRule.CAPABILITY_GROUP + ":" + CAPABILITY_NAME;

    public Log4J2Implementation() {
        super(CAPABILITY_NAME);
    }
}
