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

import org.gradle.api.Action;
import org.gradle.api.artifacts.ComponentMetadataContext;
import org.gradle.api.artifacts.VariantMetadata;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.attributes.Usage;
import org.gradle.internal.component.external.model.ModuleComponentResolveMetadata;
import org.gradle.internal.component.model.VariantResolveMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class VariantSelection {

    static class MavenVariant {
        String name;
        Map<String, String> attributes = new HashMap<>();

        MavenVariant(String name) {
            this.name = name;
        }

        MavenVariant attribute(String name, String value) {
            attributes.put(name, value);
            return this;
        }
    }

    static final MavenVariant MAVEN_RUNTIME_VARIANT = new MavenVariant("runtime")
            .attribute(Usage.USAGE_ATTRIBUTE.getName(), Usage.JAVA_RUNTIME)
            .attribute(Category.CATEGORY_ATTRIBUTE.getName(), Category.LIBRARY)
            .attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE.getName(), LibraryElements.JAR);
    static final MavenVariant MAVEN_COMPILE_VARIANT = new MavenVariant("compile")
            .attribute(Usage.USAGE_ATTRIBUTE.getName(), Usage.JAVA_API)
            .attribute(Category.CATEGORY_ATTRIBUTE.getName(), Category.LIBRARY)
            .attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE.getName(), LibraryElements.JAR);


    private VariantSelection() { }

    static void allVariantsMatching(ComponentMetadataContext context, Predicate<VariantIdentification> id, Action<? super VariantMetadata> action) {
        List<String> variantNames = discoverNames(context, id);
        variantNames.forEach(variantName -> context.getDetails().withVariant(variantName, action));
    }

    private static List<String> discoverNames(ComponentMetadataContext context, Predicate<VariantIdentification> id) {
        ModuleComponentResolveMetadata metadata = extractMetadataFromContext(context);
        List<VariantResolveMetadata> variants = getVariants(metadata);

        if (variants.isEmpty()) {
            return Stream.of(MAVEN_RUNTIME_VARIANT, MAVEN_COMPILE_VARIANT)
                    .filter(v -> id.test(new VariantIdentification(v.attributes)))
                    .map(v -> v.name)
                    .collect(Collectors.toList());
        } else {
            return variants.stream()
                    .filter(v -> id.test(new VariantIdentification(toMap(getAttributes(v)))))
                    .map(VariantResolveMetadata::getName)
                    .collect(Collectors.toList());
        }
    }

    private static Map<String, String> toMap(AttributeContainer attributes) {
        return attributes.keySet().stream().collect(Collectors
                .toMap(Attribute::getName, k -> requireNonNull(attributes.getAttribute(k)).toString()));
    }

    private static AttributeContainer getAttributes(VariantResolveMetadata metadata) {
        try {
            Method getVariants = VariantResolveMetadata.class.getDeclaredMethod("getAttributes");
            return (AttributeContainer) getVariants.invoke(metadata);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<VariantResolveMetadata> getVariants(ModuleComponentResolveMetadata metadata) {
        try {
            Method getVariants = ModuleComponentResolveMetadata.class.getDeclaredMethod("getVariants");
            @SuppressWarnings("unchecked")
            List<VariantResolveMetadata> variants = (List<VariantResolveMetadata>) getVariants.invoke(metadata);
            return variants;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static ModuleComponentResolveMetadata extractMetadataFromContext(ComponentMetadataContext context) {
        try {
            Field metadataField = context.getClass().getDeclaredField("metadata");
            metadataField.setAccessible(true);
            return (ModuleComponentResolveMetadata) metadataField.get(context);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
