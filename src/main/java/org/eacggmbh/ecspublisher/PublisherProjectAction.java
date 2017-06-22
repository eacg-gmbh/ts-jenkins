package org.eacggmbh.ecspublisher;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project action class
 *
 * @author Varanytsia Anatolii
 */
public class PublisherProjectAction implements Action {
    /**
     * project
     */
    private AbstractProject<?, ?> project;
    private Run<?, ?> build;
    final private Class<PublisherAction> buildClass = PublisherAction.class;

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
        return Messages.ProjectAction_displayName();
    }

    /**
     * Get url name
     *
     * @return name
     */
    @Override
    public String getUrlName() {
        return "ecs-analysis-last";
    }

    /**
     * Get project
     *
     * @return project
     */
    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    /**
     * Get project name
     *
     * @return name
     */
    public String getProjectName() {
        return this.project.getName();
    }

    /**
     * Get build number
     *
     * @return build number
     */
    public int getBuildNumber() {
        return getBuild() != null ? getBuild().number : null;
    }

    /**
     * Get scans
     *
     * @return scans
     */
    public Map<String, PublisherScan> getScans() {
        return getBuild() != null ? getBuild().getAction(buildClass).getScans() : null;
    }

    /**
     * Reset build
     */
    public void resetBuild(){
        this.build = null;
    }

    /**
     * Get build
     *
     * @return build
     */
    public Run<?, ?> getBuild() {
        if (build != null) {
            return build;
        }
        List<? extends AbstractBuild<?, ?>> builds = project.getBuilds();
        for (AbstractBuild<?, ?> currentBuild : builds) {
            if (currentBuild.getAction(buildClass) != null &&
                    currentBuild.getAction(buildClass).getScans() != null) {
                build = currentBuild;
                return currentBuild;
            }
        }
        return null;
    }

    /**
     * Constructor
     *
     * @param project project
     */
    PublisherProjectAction(final AbstractProject<?, ?> project) {
        this.project = project;
        this.build = null;
    }
}
