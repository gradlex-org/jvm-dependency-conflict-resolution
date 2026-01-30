// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.resolution;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DefaultResolutionStrategy {
    HIGHEST_VERSION,
    FIRST_MODULE,
    NONE
}
