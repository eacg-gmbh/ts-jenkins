package org.eacggmbh.ecspublisher;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Class to save credentials information.
 *
 * @author Varanytsia Anatolii
 */
public class PublisherCredentials extends AbstractDescribableImpl<PublisherCredentials> {
    /**
     * UserAgent
     */
    private final String userAgent = "org/eacggmbh/ecspublisher/1.0.0";
    /**
     * Default url
     */
    private final String defaultUrl = "https://ecs-app.eacg.de";
    /**
     * Api token
     */
    private final String apiToken;
    /**
     * User name
     */
    private final String userName;
    /**
     * Base url
     */
    private final String baseUrl;

    /**
     * Constructor.
     *
     * @param apiToken apiToken
     * @param userName userName
     * @param baseUrl  baseUrl
     */
    @DataBoundConstructor
    public PublisherCredentials(String apiToken, String userName, String baseUrl) {
        this.apiToken = apiToken;
        this.userName = userName;
        this.baseUrl = baseUrl;
    }

    /**
     * Constructor
     */
    public PublisherCredentials() {
        this.apiToken = "";
        this.userName = "";
        this.baseUrl = "";
    }

    /**
     * Get api token
     *
     * @return apiToken
     */
    public String getApiToken() {
        return apiToken;
    }

    /**
     * Get user name
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get base url
     *
     * @return baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Get url
     *
     * @return defaultUrl or baseUrl
     */
    public String getUrl() {
        return baseUrl == null || baseUrl.equals("") ? defaultUrl : baseUrl;
    }

    /**
     * Get userAgent
     *
     * @return userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Check equals
     *
     * @param obj object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PublisherCredentials other = (PublisherCredentials) obj;
        if ((this.apiToken == null) ? (other.apiToken != null) : !this.apiToken.equals(other.apiToken)) {
            return false;
        }
        if ((this.userName == null) ? (other.userName != null) : !this.userName.equals(other.userName)) {
            return false;
        }
        if ((this.baseUrl == null) ? (other.baseUrl != null) : !this.baseUrl.equals(other.baseUrl)) {
            return false;
        }
        return true;
    }

    /**
     * Descriptor
     */
    @Extension
    public static class DescriptorImpl extends Descriptor<PublisherCredentials> {
        /**
         * Check api token
         *
         * @param apiToken apiToken
         * @param userName userName
         * @param baseUrl  baseUrl
         * @return FormValidation
         */
        public FormValidation doCheckApiToken(@QueryParameter String apiToken, @QueryParameter String userName, @QueryParameter String baseUrl) {
            if (!apiToken.isEmpty() && !userName.isEmpty()) {
                RestClient client = new RestClient(new PublisherCredentials(apiToken, userName, baseUrl));
                if (!client.isAuthorized()) {
                    return FormValidation.error("API token is wrong!");
                }
            }
            return FormValidation.validateRequired(apiToken);
        }

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