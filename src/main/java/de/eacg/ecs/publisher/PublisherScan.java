package de.eacg.ecs.publisher;

import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Scan object
 *
 * @author Varanytsia Anatolii
 */
public class PublisherScan {
    /**
     * Scan id
     */
    private final String scanId;
    /**
     * Project name
     */
    private final String project;
    /**
     * Plugin object
     */
    private final Map<String, String> plugin;
    /**
     * Result
     */
    private JSONObject result;

    /**
     * Get scan id
     *
     * @return scanId
     */
    public String getScanId() {
        return this.scanId;
    }

    /**
     * Get project
     *
     * @return project name
     */
    public String getProject() {
        return this.project;
    }

    /**
     * Get plugin
     *
     * @return plugin object
     */
    public Map<String, String> getPlugin() {
        return this.plugin;
    }

    /**
     * Get result
     *
     * @return result
     */
    public JSONObject getResult() {
        return this.result;
    }

    /**
     * Get statistics
     *
     * @return statistics object
     */
    public JSONObject getStatistics() {
        return this.result.getJSONObject("statistics");
    }

    /**
     * Get components count
     *
     * @return count
     */
    public int getComponents() {
        return getStatistics().getInt("components");
    }

    /**
     * Get licenses count
     *
     * @return count
     */
    public int getLicenses() {
        return getStatistics().getInt("licenses");
    }

    /**
     * Get vulnerability violations
     *
     * @return count
     */
    public int getVulnerabilityViolations() {
        return getStatistics().getJSONObject("vulnerability").getInt("violations");
    }

    /**
     * Get vulnerability warnings
     *
     * @return count
     */
    public int getVulnerabilityWarnings() {
        return getStatistics().getJSONObject("vulnerability").getInt("warnings");
    }

    /**
     * Get legal violations
     *
     * @return count
     */
    public int getLegalViolations() {
        return getStatistics().getJSONObject("legal").getInt("violations");
    }

    /**
     * Get legal warnings
     *
     * @return count
     */
    public int getLegalWarnings() {
        return getStatistics().getJSONObject("legal").getInt("warnings");
    }

    /**
     * Get vulnerability violations percent
     *
     * @return percentage
     */
    public double getVulnerabilityViolationsPercent() {
        return (double) getVulnerabilityViolations() / getComponents();
    }

    /**
     * Get vulnerability warningsPercent
     *
     * @return percentage
     */
    public double getVulnerabilityWarningsPercent() {
        return (double) getVulnerabilityWarnings() / getComponents();
    }

    /**
     * Get legal violations percent
     *
     * @return percentage
     */
    public double getLegalViolationsPercent() {
        return (double) getLegalViolations() / getLicenses();
    }

    /**
     * Get legal warnings percent
     *
     * @return percentage
     */
    public double getLegalWarningsPercent() {
        return (double) getLegalWarnings() / getLicenses();
    }

    /**
     * Set result object
     *
     * @param result result
     */
    public void setResult(JSONObject result) {
        this.result = result;
    }

    /**
     * Constructor
     *
     * @param scanId scanId
     * @param plugin plugin
     */
    PublisherScan(String scanId, String project, Map<String, String> plugin) {
        this.scanId = scanId;
        this.project = project;
        this.plugin = plugin;
    }
}
