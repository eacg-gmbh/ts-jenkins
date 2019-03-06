package de.eacg.ecs.publisher;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import hudson.util.Secret;

/**
 * Class to save credentials information.
 *
 * @author Varanytsia Anatolii
 */
public class PublisherCredentials extends AbstractDescribableImpl<PublisherCredentials> {
    /**
     * UserAgent
     */
    private final String userAgent = "de/eacg/ecs/publisher/1.0.0";
    /**
     * Default url
     */
    private final String defaultUrl = "https://ecs-app.eacg.de";
    /**
     * Api token
     */
    private final Secret apiToken;
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
        this.apiToken = Secret.fromString(apiToken);
        this.userName = userName;
        this.baseUrl = baseUrl;
    }

    /**
     * Constructor
     */
    public PublisherCredentials() {
        this.apiToken = Secret.fromString("");
        this.userName = "";
        this.baseUrl = "";
    }

    /**
     * Get api token
     *
     * @return apiToken
     */
    public Secret getApiToken() {
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
        if (obj == this) return true;
        if (!(obj instanceof PublisherCredentials)) {
            return false;
        }
        final PublisherCredentials publisherCredentials = (PublisherCredentials) obj;
        return new EqualsBuilder()
                .append(apiToken, publisherCredentials.apiToken)
                .append(userName, publisherCredentials.userName)
                .append(baseUrl, publisherCredentials.baseUrl)
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
                .append(apiToken)
                .append(userName)
                .append(baseUrl)
                .toHashCode();
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