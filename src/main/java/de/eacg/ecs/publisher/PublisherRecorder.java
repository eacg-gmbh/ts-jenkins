package de.eacg.ecs.publisher;

import hudson.Launcher;
import hudson.Extension;
import hudson.model.*;
import hudson.tasks.Recorder;
import hudson.tasks.Publisher;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Step to publish reports to ECS.
 *
 * @author Varanytsia Anatolii
 */
public class PublisherRecorder extends Recorder {
    /**
     * Project name
     */
    private final String project;
    /**
     * List of PublisherPath
     */
    private final ArrayList<PublisherPath> overridePaths;
    /**
     * Credentials
     */
    private PublisherCredentials overrideCredentials;
    /**
     * BreakOptions
     */
    private PublisherBreakOptions overrideBreakOptions;

    /**
     * Constructor
     *
     * @param project           project
     * @param overridePathsBool overridePathsBool
     * @param overridePaths     overridePaths
     */
    @DataBoundConstructor
    public PublisherRecorder(String project, Boolean overridePathsBool, List<PublisherPath> overridePaths) {
        this.project = project;
        this.overridePaths = overridePaths != null && overridePathsBool ? new ArrayList<PublisherPath>(overridePaths) : new ArrayList<PublisherPath>();
    }

    /**
     * Set optional params credentials.
     *
     * @param overrideCredentials overrideCredentials
     */
    @DataBoundSetter
    public void setOverrideCredentials(PublisherCredentials overrideCredentials) {
        this.overrideCredentials = overrideCredentials;
    }

    /**
     * Set optional params breakOptions.
     *
     * @param overrideBreakOptions overrideBreakOptions
     */
    @DataBoundSetter
    public void setOverrideBreakOptions(PublisherBreakOptions overrideBreakOptions) {
        this.overrideBreakOptions = overrideBreakOptions;
    }

    /**
     * Get project
     *
     * @return project name
     */
    public String getProject() {
        return project;
    }

    /**
     * Get override paths
     *
     * @return overridePaths
     */
    public ArrayList<PublisherPath> getOverridePaths() {
        return overridePaths;
    }

    /**
     * Get override credentials
     *
     * @return overrideCredentials
     */
    public PublisherCredentials getOverrideCredentials() {
        return overrideCredentials;
    }

    /**
     * Get override break options
     *
     * @return overrideBreakOptions
     */
    public PublisherBreakOptions getOverrideBreakOptions() {
        return overrideBreakOptions;
    }

    /**
     * Get credentials
     *
     * @return credentials from global config or from overrideCredentials
     */
    public PublisherCredentials getCredentials() {
        if (getOverrideCredentials() != null) {
            return getOverrideCredentials();
        }
        if(getDescriptor() != null && getDescriptor().getCredentials() != null) {
            return getDescriptor().getCredentials();
        }
        return new PublisherCredentials();
    }

    /**
     * Get break options
     *
     * @return breakOptions from global config or from overrideBreakOptions
     */
    public PublisherBreakOptions getBreakOptions() {
        if (getOverrideBreakOptions() != null) {
            return getOverrideBreakOptions();
        }
        if(getDescriptor() != null && getDescriptor().getBreakOptions() != null) {
            return getDescriptor().getBreakOptions();
        }
        return new PublisherBreakOptions();
    }

    /**
     * Perform publish step
     *
     * @param build    build
     * @param launcher launcher
     * @param listener listener
     * @return boolean
     * @throws InterruptedException InterruptedException
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException {
        PublisherStepExecution publisherStepExecution = new PublisherStepExecution(build, build.getWorkspace(), launcher, listener, listener.getLogger(), getProject(), getOverridePaths(), getCredentials(), getBreakOptions());
        return publisherStepExecution.run();
    }

    /**
     * Get required monitor service
     *
     * @return BuildStepMonitor.NONE
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    /**
     * Get descriptor
     *
     * @return descriptor
     */
    @Override
    public PublisherRecorder.DescriptorImpl getDescriptor() {
        return (PublisherRecorder.DescriptorImpl) super.getDescriptor();
    }

    /**
     * Get project action
     *
     * @param project project
     * @return new project action
     */
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new PublisherProjectAction(project);
    }

    /**
     * Descriptor for {@link PublisherRecorder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     */
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * Global credentials
         */
        private PublisherCredentials credentials;
        /**
         * Global break options
         */
        private PublisherBreakOptions breakOptions;

        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * isApplicable
         *
         * @param aClass aClass
         * @return true
         */
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         *
         * @return name
         */
        public String getDisplayName() {
            return "ECS publisher";
        }

        /**
         * Save global information
         *
         * @param req      request
         * @param formData formData
         * @return boolean
         * @throws FormException FormException
         */
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            credentials = req.bindJSON(PublisherCredentials.class, formData.getJSONObject("credentials"));
            breakOptions = req.bindJSON(PublisherBreakOptions.class, formData.getJSONObject("breakOptions"));
            save();
            return super.configure(req, formData);
        }

        /**
         * Get Credentials
         *
         * @return credentials
         */
        public PublisherCredentials getCredentials() {
            return credentials;
        }

        /**
         * Get Break Options
         *
         * @return breakOptions
         */
        public PublisherBreakOptions getBreakOptions() {
            return breakOptions;
        }
    }
}

