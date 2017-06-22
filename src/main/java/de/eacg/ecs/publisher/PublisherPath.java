package de.eacg.ecs.publisher;

import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.Extension;
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PublisherPath other = (PublisherPath) obj;
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        return true;
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
