// SPDX-License-Identifier: Apache-2.0
package org.gradlex.jvm.dependency.conflict.detection.util;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Inspired by: org.gradle.util.internal.VersionNumber
 */
public class VersionNumber implements Comparable<VersionNumber> {

    private static final VersionNumber UNKNOWN = version(0, 0);

    private final int major;
    private final int minor;
    private final int micro;
    private final int patch;
    private final String qualifier;

    private VersionNumber(int major, int minor, int micro, int patch, @Nullable String qualifier) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.patch = patch;
        this.qualifier = qualifier;
    }

    @Override
    public int compareTo(VersionNumber other) {
        if (major != other.major) {
            return major - other.major;
        }
        if (minor != other.minor) {
            return minor - other.minor;
        }
        if (micro != other.micro) {
            return micro - other.micro;
        }
        if (patch != other.patch) {
            return patch - other.patch;
        }
        return toLowerCase(qualifier).compareTo(toLowerCase(other.qualifier));
    }

    private String toLowerCase(@Nullable String string) {
        return string == null ? "" : string.toLowerCase();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof VersionNumber && compareTo((VersionNumber) other) == 0;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + micro;
        result = 31 * result + patch;
        result = 31 * result + Objects.hashCode(qualifier);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d%s", major, minor, micro, qualifier == null ? "" : "-" + qualifier);
    }

    public static VersionNumber version(int major, int minor) {
        return new VersionNumber(major, minor, 0, 0, null);
    }

    public static VersionNumber parse(String versionString) {
        if (versionString == null || versionString.isEmpty()) {
            return UNKNOWN;
        }
        Scanner scanner = new Scanner(versionString);

        int major;
        int minor = 0;
        int micro = 0;
        int patch = 0;

        if (!scanner.hasDigit()) {
            return UNKNOWN;
        }
        major = scanner.scanDigit();
        if (scanner.isSeparatorAndDigit('.')) {
            scanner.skipSeparator();
            minor = scanner.scanDigit();
            if (scanner.isSeparatorAndDigit('.')) {
                scanner.skipSeparator();
                micro = scanner.scanDigit();
            }
        }

        if (scanner.isEnd()) {
            return new VersionNumber(major, minor, micro, patch, null);
        }

        if (scanner.isQualifier()) {
            scanner.skipSeparator();
            return new VersionNumber(major, minor, micro, patch, scanner.remainder());
        }

        return UNKNOWN;
    }

    private static class Scanner {
        int pos;
        final String str;

        private Scanner(String string) {
            this.str = string;
        }

        boolean hasDigit() {
            return pos < str.length() && Character.isDigit(str.charAt(pos));
        }

        boolean isSeparatorAndDigit(char... separators) {
            return pos < str.length() - 1 && oneOf(separators) && Character.isDigit(str.charAt(pos + 1));
        }

        private boolean oneOf(char... separators) {
            char current = str.charAt(pos);
            for (char separator : separators) {
                if (current == separator) {
                    return true;
                }
            }
            return false;
        }

        boolean isQualifier() {
            return pos < str.length() - 1 && oneOf('.', '-');
        }

        int scanDigit() {
            int start = pos;
            while (hasDigit()) {
                pos++;
            }
            return Integer.parseInt(str.substring(start, pos));
        }

        public boolean isEnd() {
            return pos == str.length();
        }

        public void skipSeparator() {
            pos++;
        }

        @Nullable
        public String remainder() {
            return pos == str.length() ? null : str.substring(pos);
        }
    }
}
