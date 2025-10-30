// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution.rules;

import java.util.Map;
import java.util.Objects;
import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

class VariantIdentification {

    private final Map<String, String> attributes;

    VariantIdentification(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    boolean matches(Attribute<? extends Named> attribute, String value) {
        return matches(attribute.getName(), value);
    }

    boolean matches(String attribute, String value) {
        return Objects.equals(attributes.get(attribute), value);
    }
}
