package de.eacg.ecs.publisher;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.Extension;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Class to save path to plugin.
 *
 * @author Varanytsia Anatolii
 */
public class PublisherPath extends AbstractDescribableImpl<PublisherPath> {
    private final String path;

    /**
     * Constructor.
     *
     * @param path Path to plugin
     */
    @DataBoundConstructor
    public PublisherPath(String path) {
        this.path = path;
    }

    /**
     * Get path
     *
     * @return path
     */
    public String getPath() {
        return this.path;
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
        if (!(obj instanceof PublisherPath)) {
            return false;
        }
        final PublisherPath publisherPath = (PublisherPath) obj;
        return new EqualsBuilder()
                .append(path, publisherPath.path)
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
                .append(path)
                .toHashCode();
    }

    /**
     * Descriptor
     */
    @Extension
    public static class DescriptorImpl extends Descriptor<PublisherPath> {
        /**
         * Get display name
         *
         * @return name
         */
        public String getDisplayName() {
            return "";
        }
    }
}
