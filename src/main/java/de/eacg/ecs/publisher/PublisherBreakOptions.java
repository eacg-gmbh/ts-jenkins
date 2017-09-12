package de.eacg.ecs.publisher;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Class to save break options.
 *
 * @author Varanytsia Anatolii
 */
public class PublisherBreakOptions extends AbstractDescribableImpl<PublisherBreakOptions> {
    /**
     * Allow break build
     */
    private final Boolean allowBreakBuild;
    /**
     * Break on vulnerabilities
     */
    private final Boolean breakOnVulnerabilities;
    /**
     * Break on vulnerabilities value
     */
    private final String breakOnVulnerabilitiesValue;
    /**
     * Break on legal issues
     */
    private final Boolean breakOnLegalIssues;
    /**
     * Break on legal issues value
     */
    private final String breakOnLegalIssuesValue;
    /**
     * Break on viability issues
     */
    private final Boolean breakOnViabilityIssues;
    /**
     * Break on viability issues value
     */
    private final String breakOnViabilityIssuesValue;

    /**
     * all constant
     */
    public static final String all = "all";
    /**
     * Warnings and critical constant
     */
    public static final String warningsAndCritical = "warningsAndCritical";
    /**
     * Critical hits only constant
     */
    public static final String criticalHitsOnly = "criticalHitsOnly";
    /**
     * Warning and violations constant
     */
    public static final String warningAndViolations = "warningAndViolations";
    /**
     * Violations only constant
     */
    public static final String violationsOnly = "violationsOnly";
    /**
     * Strong mismatches only constant
     */
    public static final String strongMismatchesOnly = "strongMismatchesOnly";

    /**
     * Constructor.
     *
     * @param allowBreakBuild             allowBreakBuild
     * @param breakOnVulnerabilities      breakOnVulnerabilities
     * @param breakOnVulnerabilitiesValue breakOnVulnerabilitiesValue
     * @param breakOnLegalIssues          breakOnLegalIssues
     * @param breakOnLegalIssuesValue     breakOnLegalIssuesValue
     * @param breakOnViabilityIssues      breakOnViabilityIssues
     * @param breakOnViabilityIssuesValue breakOnViabilityIssuesValue
     */
    @DataBoundConstructor
    public PublisherBreakOptions(
            Boolean allowBreakBuild, Boolean breakOnVulnerabilities, String breakOnVulnerabilitiesValue,
            Boolean breakOnLegalIssues, String breakOnLegalIssuesValue, Boolean breakOnViabilityIssues,
            String breakOnViabilityIssuesValue) {
        this.allowBreakBuild = allowBreakBuild;
        this.breakOnVulnerabilities = breakOnVulnerabilities;
        this.breakOnVulnerabilitiesValue = breakOnVulnerabilitiesValue;
        this.breakOnLegalIssues = breakOnLegalIssues;
        this.breakOnLegalIssuesValue = breakOnLegalIssuesValue;
        this.breakOnViabilityIssues = breakOnViabilityIssues;
        this.breakOnViabilityIssuesValue = breakOnViabilityIssuesValue;
    }

    /**
     * Constructor
     */
    public PublisherBreakOptions() {
        this.allowBreakBuild = false;
        this.breakOnVulnerabilities = false;
        this.breakOnVulnerabilitiesValue = all;
        this.breakOnLegalIssues = false;
        this.breakOnLegalIssuesValue = all;
        this.breakOnViabilityIssues = false;
        this.breakOnViabilityIssuesValue = all;
    }

    /**
     * is allow break build
     *
     * @return boolean
     */
    public Boolean isAllowBreakBuild() {
        return allowBreakBuild;
    }

    /**
     * Is break on vulnerabilities
     *
     * @return boolean
     */
    public Boolean isBreakOnVulnerabilities() {
        return breakOnVulnerabilities;
    }

    /**
     * Is break on vulnerabilities
     *
     * @param value value
     * @return boolean
     */
    public Boolean isBreakOnVulnerabilities(String value) {
        return breakOnVulnerabilitiesValue != null && breakOnVulnerabilitiesValue.equals(value);
    }

    /**
     * Is break on vulnerabilities warnings and critical
     *
     * @return boolean
     */
    public Boolean isBreakOnVulnerabilitiesWarningsAndCritical() {
        return breakOnVulnerabilitiesValue == null || isBreakOnVulnerabilities(warningsAndCritical);
    }

    /**
     * Is break on vulnerabilities critical hits only
     *
     * @return boolean
     */
    public Boolean isBreakOnVulnerabilitiesCriticalHitsOnly() {
        return isBreakOnVulnerabilities(criticalHitsOnly);
    }

    /**
     * Get break on vulnerabilities value
     *
     * @return value
     */
    public String getBreakOnVulnerabilitiesValue() {
        return breakOnVulnerabilitiesValue;
    }

    /**
     * Is break on legal issues
     *
     * @return boolean
     */
    public Boolean isBreakOnLegalIssues() {
        return breakOnLegalIssues;
    }

    /**
     * Is break on legal issues
     *
     * @param value value
     * @return boolean
     */
    public Boolean isBreakOnLegalIssues(String value) {
        return breakOnLegalIssuesValue != null && breakOnLegalIssuesValue.equals(value);
    }

    /**
     * Is break on legal issues warning and violations
     *
     * @return boolean
     */
    public Boolean isBreakOnLegalIssuesWarningAndViolations() {
        return breakOnLegalIssuesValue == null || isBreakOnLegalIssues(warningAndViolations);
    }

    /**
     * Is break on legal issues violations only
     *
     * @return boolean
     */
    public Boolean isBreakOnLegalIssuesViolationsOnly() {
        return isBreakOnLegalIssues(violationsOnly);
    }

    /**
     * Get break on legal issues value
     *
     * @return value
     */
    public String getBreakOnLegalIssuesValue() {
        return breakOnLegalIssuesValue;
    }

    /**
     * Is break on viability issues
     *
     * @return boolean
     */
    public Boolean isBreakOnViabilityIssues() {
        return breakOnViabilityIssues;
    }

    /**
     * Is break on viability issues
     *
     * @param value value
     * @return boolean
     */
    public Boolean isBreakOnViabilityIssues(String value) {
        return breakOnViabilityIssuesValue != null && breakOnViabilityIssuesValue.equals(value);
    }

    /**
     * Is break on viability issues all
     *
     * @return boolean
     */
    public Boolean isBreakOnViabilityIssuesAll() {
        return breakOnViabilityIssuesValue == null || isBreakOnViabilityIssues(all);
    }

    /**
     * Is break on viability issues strong mismatches only
     *
     * @return boolean
     */
    public Boolean isBreakOnViabilityIssuesStrongMismatchesOnly() {
        return isBreakOnViabilityIssues(strongMismatchesOnly);
    }

    /**
     * Get break on viability issues value
     *
     * @return value
     */
    public String getBreakOnViabilityIssuesValue() {
        return breakOnViabilityIssuesValue;
    }

    /**
     * Check equals
     *
     * @param obj object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PublisherBreakOptions)) {
            return false;
        }
        final PublisherBreakOptions publisherBreakOptions = (PublisherBreakOptions) obj;
        return new EqualsBuilder()
                .append(allowBreakBuild, publisherBreakOptions.allowBreakBuild)
                .append(breakOnVulnerabilities, publisherBreakOptions.breakOnVulnerabilities)
                .append(breakOnVulnerabilitiesValue, publisherBreakOptions.breakOnVulnerabilitiesValue)
                .append(breakOnLegalIssues, publisherBreakOptions.breakOnLegalIssues)
                .append(breakOnLegalIssuesValue, publisherBreakOptions.breakOnLegalIssuesValue)
                .append(breakOnViabilityIssues, publisherBreakOptions.breakOnViabilityIssues)
                .append(breakOnViabilityIssuesValue, publisherBreakOptions.breakOnViabilityIssuesValue)
                .isEquals();
    }

    /**
     * Return hashCode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(allowBreakBuild)
                .append(breakOnVulnerabilities)
                .append(breakOnVulnerabilitiesValue)
                .append(breakOnLegalIssues)
                .append(breakOnLegalIssuesValue)
                .append(breakOnViabilityIssues)
                .append(breakOnViabilityIssuesValue)
                .toHashCode();
    }

    /**
     * Descriptor
     */
    @Extension
    public static class DescriptorImpl extends Descriptor<PublisherBreakOptions> {
        /**
         * Get display name
         *
         * @return name
         */
        public String getDisplayName() {
            return "Path";
        }
    }
}