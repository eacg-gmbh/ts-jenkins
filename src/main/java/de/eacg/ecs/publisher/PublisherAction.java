package de.eacg.ecs.publisher;

import hudson.model.Run;
import hudson.model.Action;

import java.util.Map;

/**
 * Build action class
 *
 * @author Varanytsia Anatolii
 */
public class PublisherAction implements Action {
    /**
     * Build
     */
    private Run<?, ?> build;
    /**
     * Scans
     */
    private final Map<String, PublisherScan> scans;

    /**
     * Get icon file name
     *
     * @return name
     */
    @Override
    public String getIconFileName() {
        return "graph.gif";
    }

    /**
     * Get display name
     *
     * @return name
     */
    @Override
    public String getDisplayName() {
        return Messages.BuildAction_displayName();
    }

    /**
     * Get url name
     *
     * @return name
     */
    @Override
    public String getUrlName() {
        return "ecs-analysis";
    }

    /**
     * Get build number
     *
     * @return build number
     */
    public int getBuildNumber() {
        return getBuild().number;
    }

    /**
     * Get scans
     *
     * @return scans
     */
    public Map<String, PublisherScan> getScans() {
        return scans;
    }

    /**
     * Get build
     *
     * @return build
     */
    public Run<?, ?> getBuild() {
        return build;
    }

    /**
     * Constructor
     *
     * @param build build
     * @param scans build
     */
    PublisherAction(final Run<?, ?> build, final Map<String, PublisherScan> scans) {
        this.build = build;
        this.scans = scans;
    }
}
