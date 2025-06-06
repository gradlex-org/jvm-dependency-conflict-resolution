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

package org.gradlex.jvm.dependency.conflict.resolution.rules;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

import java.util.Map;
import java.util.Objects;

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
