package org.personal.mason.ass.common.jpa;

/**
 * Created by mason on 8/12/14.
 */
public interface ComparisonStyle {
    public static enum StringComparisonMode {
        EXACT,            // compares exactly preserving case
        CASE_INSENSITIVE, // compares ignoring case
        LIKE              // compares with LIKE() operator
    }

    /**
     * Affirms if the attribute comparisons are OR'ed.
     * Defaults to false.
     */
    boolean isDisjunction();

    /**
     * Sets whether the comparison to OR the attribute comparisons.
     */
    ComparisonStyle setDisjunction(boolean flag);

    /**
     * Affirms if the null-valued attribute be excluded from comparison.
     * Defaults to true.
     */
    boolean excludeNull();

    /**
     * Sets whether the comparison excludes null-valued attributes.
     */
    ComparisonStyle setExcludeNull(boolean flag);

    /**
     * Affirms if the identity attribute be excluded from comparison.
     * Defaults to true.
     */
    boolean excludeIdentity();

    /**
     * Sets whether the comparison excludes identity attribute.
     */
    ComparisonStyle setExcludeIdentity(boolean flag);

    /**
     * Affirms if the version attribute be excluded from comparison.
     * Defaults to true.
     */
    boolean excludeVersion();

    /**
     * Sets whether the comparison excludes version attribute.
     */
    ComparisonStyle setExcludeVersion(boolean flag);

    /**
     * Affirms if the default-valued attribute be excluded from comparison.
     * Defaults to true.
     */

    boolean excludeDefault();

    /**
     * Sets whether the comparison excludes default-valued attributes.
     */
    ComparisonStyle setExcludeDefault(boolean flag);

    /**
     * Gets how string-valued attributes be compared.
     */
    StringComparisonMode getStringComparsionMode();

    /**
     * Sets the comparison mode for String-valued attributes.
     */
    ComparisonStyle setStringComparisonMode(StringComparisonMode mode);

    /**
     * Default implementation.
     *
     */
    static class Default implements ComparisonStyle {
        private boolean excludeDefault = true;
        private boolean excludeNull = true;
        private boolean excludeIdentity = true;
        private boolean excludeVersion = true;
        private boolean disjunction = false;
        private StringComparisonMode stringMode = StringComparisonMode.EXACT;

        public boolean excludeDefault() {
            return excludeDefault;
        }

        public boolean excludeNull() {
            return excludeNull;
        }

        public StringComparisonMode getStringComparsionMode() {
            return stringMode;
        }

        public boolean isDisjunction() {
            return disjunction;
        }

        public ComparisonStyle setExcludeDefault(boolean flag) {
            excludeDefault = flag;
            return this;
        }

        public ComparisonStyle setExcludeNull(boolean flag) {
            excludeNull = flag;
            return this;
        }

        public ComparisonStyle setStringComparisonMode(StringComparisonMode mode) {
            stringMode = mode;
            return this;
        }

        public ComparisonStyle setDisjunction(boolean flag) {
            disjunction = flag;
            return this;
        }

        public boolean excludeIdentity() {
            return excludeIdentity;

        }

        public ComparisonStyle setExcludeIdentity(boolean flag) {
            excludeIdentity = flag;
            return this;
        }

        public boolean excludeVersion() {
            return excludeVersion;

        }

        public ComparisonStyle setExcludeVersion(boolean flag) {
            excludeVersion = flag;
            return this;
        }
    }
}
