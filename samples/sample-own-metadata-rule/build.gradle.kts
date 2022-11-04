import de.jjohannes.gradle.javaecosystem.capabilities.rules.CGlibRule

plugins {
    id("de.jjohannes.java-ecosystem-capabilities")
    id("java-library")
}

javaEcosystemCapabilities {
    deactivatedResolutionStrategies.addAll(allCapabilities)
}

dependencies {
    implementation("cglib:cglib-nodep:3.2.10")
    implementation("cglib:cglib:3.2.10")

    components.withModule(CGlibRule.MODULES[0]) {
        allVariants {
            withCapabilities {
                removeCapability(CGlibRule.CAPABILITY_GROUP, CGlibRule.CAPABILITY_NAME)
            }
        }
    }
}