package de.eacg.ecs.publisher;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.*;
import hudson.util.ArgumentListBuilder;
import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Publisher Step execution class
 *
 * @author Varanytsia Anatolii
 */
public class PublisherStepExecution {
    /**
     * Build
     */
    private Run<?, ?> build;
    /**
     * workspace
     */
    private FilePath workspace;
    /**
     * Logger
     */
    private PrintStream logger;
    /**
     * Launcher
     */
    private Launcher launcher;
    /**
     * Listener
     */
    private TaskListener listener;

    /**
     * Project name
     */
    String project;
    /**
     * Credentials
     */
    private PublisherCredentials credentials;
    /**
     * Break options
     */
    private PublisherBreakOptions breakOptions;
    /**
     * Paths
     */
    private ArrayList<PublisherPath> paths;
    /**
     * RestClient
     */
    RestClient client;
    /**
     * Scans
     */
    Map<String, PublisherScan> scans = new HashMap<String, PublisherScan>();
    /**
     * Patterns constant
     */
    public static final String[] PATTERNS = {"\\{\"scanId\":\"([^\"]*)\"}", "scanId => ([^ \n]*)"};
    /**
     * Version pattern constant
     */
    public static final String VERSION_PATTERN = "([^ ]*) version ([^\"]*)";

    /**
     * Plugin list constant
     */
    private static final Map<String, Map<String, String>> pluginsList;

    static {
        Map<String, Map<String, String>> aPlugins = new HashMap<String, Map<String, String>>();
        Map<String, String> aPlugin = new HashMap<String, String>();
        aPlugin.put("name", "default_plugin");
        aPlugin.put("version", "1.0.0");
        aPlugin.put("file", null);
        aPlugin.put("command", null);
        aPlugin.put("args", " -k %s -u %s --url %s -p %s");
        aPlugins.put(aPlugin.get("name"), aPlugin);
        aPlugin = new HashMap<String, String>();
        aPlugin.put("name", "eacg-gmbh/ecs-composer");
        aPlugin.put("version", "1.0.1");
        aPlugin.put("file", "composer.json");
        aPlugin.put("command", "vendor/bin/ecs-composer");
        aPlugin.put("args", " -k %s -u %s --url %s -p %s");
        aPlugins.put(aPlugin.get("name"), aPlugin);
        aPlugin = new HashMap<String, String>();
        aPlugin.put("name", "ecs_bundler");
        aPlugin.put("version", "1.0.1");
        aPlugin.put("file", "Gemfile");
        aPlugin.put("command", "ecs_bundler");
        aPlugin.put("args", " -k %s -u %s --url %s -p %s");
        aPlugins.put(aPlugin.get("name"), aPlugin);
        aPlugin = new HashMap<String, String>();
        aPlugin.put("name", "ecs-node-client");
        aPlugin.put("version", "0.2.0");
        aPlugin.put("file", "package.json");
        aPlugin.put("command", "node_modules/.bin/ecs-node-client");
        aPlugin.put("args", " -k %s -u %s --url %s -p %s");
        aPlugins.put(aPlugin.get("name"), aPlugin);
        //ecs_bundler -k apiKey -u userName --url base_url -p project
        //./vendor/bin/ecs-composer -k apiKey -u userName --url baseUrl -p project
        //node_modules/.bin/ecs-node-client  -k apiKey -u userName --url baseUrl -p project
        pluginsList = Collections.unmodifiableMap(aPlugins);
    }

    /**
     * Get scan id from text
     *
     * @param text text
     * @return scanId
     */
    protected String getScanId(String text) {
        for (int i = 0; i < PATTERNS.length; i++) {
            Pattern mPattern = Pattern.compile(PATTERNS[i]);
            Matcher matcher = mPattern.matcher(text);
            if (matcher == null)
                return null;
            if (matcher.find())
                return matcher.toMatchResult().group(1);
        }
        return null;
    }

    /**
     * Get name and version
     *
     * @param path path
     * @return name and version
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected MatchResult getNameAndVersion(String path) throws PublisherStepExecutionError {
        ArgumentListBuilder command = new ArgumentListBuilder();
        command.addTokenized(path);
        command.addTokenized("--version");
        String text = runCommand(command);
        Pattern mPattern = Pattern.compile(VERSION_PATTERN);
        Matcher matcher = mPattern.matcher(text);
        MatchResult nameAndVersion;
        if (matcher != null && matcher.find()) {
            nameAndVersion = matcher.toMatchResult();
            if (nameAndVersion != null) {
                return nameAndVersion;
            }
        }
        throw new PublisherStepExecutionError(Messages.PublisherStepExecution_notReturnCorrectNameAndVersion());
    }

    /**
     * Run command
     *
     * @param command command
     * @return output
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected String runCommand(ArgumentListBuilder command) throws PublisherStepExecutionError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String message;
        try {
            Launcher.ProcStarter ps = launcher.new ProcStarter();
            ps = ps.cmds(command).stdout(baos);
            ps = ps.pwd(workspace).envs(build.getEnvironment(listener));
            Proc proc = launcher.launch(ps);
            int retcode = proc.join();
            logger.print(baos.toString());
            if (retcode == 0) {
                return baos.toString();
            } else {
                message = Messages.PublisherStepExecution_commandReturn(retcode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            message = e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        throw new PublisherStepExecutionError(Messages.PublisherStepExecution_problemWithRunningCommand(command.toString()) + "\n" + message);
    }

    /**
     * Detect plugins
     *
     * @return plugins
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected List<Map<String, String>> autoDetectPlugin() throws PublisherStepExecutionError {
        List<Map<String, String>> plugins = new ArrayList<Map<String, String>>();
        logger.println(Messages.PublisherStepExecution_loggerLine() + " " + "Detecting plugins.");
        try {
            for (Map.Entry<String, Map<String, String>> entry : pluginsList.entrySet()) {
                String key = entry.getKey();
                Map<String, String> aPlugin = entry.getValue();
                if (aPlugin.get("file") != null && workspace.child(aPlugin.get("file")).exists()) {
                    MatchResult nameAndVersion = getNameAndVersion(aPlugin.get("command"));
                    Map<String, String> plugin = new HashMap<String, String>(aPlugin);
                    plugin.put("version_installed", nameAndVersion.group(2));
                    plugins.add(plugin);
                }
            }
            return plugins;
        } catch (IOException e) {
            e.printStackTrace();
            logger.println("IOException!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.println("InterruptedException!");
        }
        return new ArrayList<Map<String, String>>();
    }

    /**
     * Get plugins from paths
     *
     * @return plugins
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected List<Map<String, String>> getPluginsFromPaths() throws PublisherStepExecutionError {
        List<Map<String, String>> plugins = new ArrayList<Map<String, String>>();
        logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_autoDetectionDisabled());
        for (PublisherPath path : paths) {
            MatchResult nameAndVersion = getNameAndVersion(path.getPath());
            Map<String, String> aPlugin = pluginsList.get(nameAndVersion.group(1));
            if (aPlugin == null) {
                logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_cantFindPlugin());
                aPlugin = pluginsList.get("default_plugin");
            }
            Map<String, String> plugin = new HashMap<String, String>(aPlugin);
            plugin.put("command", path.getPath());
            plugin.put("name", nameAndVersion.group(1));
            plugin.put("version_installed", nameAndVersion.group(2));
            plugins.add(plugin);
        }
        return plugins;
    }

    /**
     * Get plugins
     *
     * @return plugins
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected List<Map<String, String>> getPlugins() throws PublisherStepExecutionError {
        return paths.size() > 0 ? getPluginsFromPaths() : autoDetectPlugin();
    }

    /**
     * Check plugins versions
     *
     * @param plugins plugins
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected void checkPluginsVersions(List<Map<String, String>> plugins) throws PublisherStepExecutionError {
        String message = "";
        for (Map<String, String> plugin : plugins) {
            if (plugin.get("version_installed").compareTo(plugin.get("version")) < 0) {
                message += (message.isEmpty() ? "" : "\n") + Messages.PublisherStepExecution_upgradeYourVersion(plugin.get("name"), plugin.get("version"));
            }
        }
        if (!message.isEmpty()) {
            throw new PublisherStepExecutionError(message);
        }
    }

    /**
     * Check credentials
     *
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected void checkCredentials() throws PublisherStepExecutionError {
        if (!client.isAuthorized()) {
            throw new PublisherStepExecutionError(Messages.PublisherStepExecution_apiTokenIsWrong());
        }
    }

    /**
     * Run plugins
     *
     * @param plugins plugins
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected void runPlugins(List<Map<String, String>> plugins) throws PublisherStepExecutionError {
        for (Map<String, String> plugin : plugins) {
            ArgumentListBuilder command = new ArgumentListBuilder();
            command.addTokenized(plugin.get("command"));
            command.addTokenized(String.format(plugin.get("args"), credentials.getApiToken(), credentials.getUserName(), credentials.getUrl(), project));
            try {
                if (plugin.get("name").equals("ecs-node-client") && workspace.child(plugin.get("command")).sibling("../../.meteor").exists()) {
                    command.addTokenized(" --meteor");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.println("IOException!");
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.println("InterruptedException!");
            }
            logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_running(command.toString()));
            String result = runCommand(command);
            String scanId = getScanId(result == null ? "" : result);
            if (result == null || scanId == null) {
                throw new PublisherStepExecutionError(Messages.PublisherStepExecution_cantGetScanId());
            } else {
                scans.put(scanId, new PublisherScan(scanId, project, plugin));
            }
        }
    }

    /**
     * Get plugins results
     *
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected void getPluginsResults() throws PublisherStepExecutionError {
        String message = "";
        for (Map.Entry<String, PublisherScan> entry : scans.entrySet()) {
            String key = entry.getKey();
            PublisherScan scan = entry.getValue();
            logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_getResultsForScanId(scan.getScanId()));
            JSONObject scanResult = client.getScanResult(scan.getScanId());
            if (scanResult == null) {
                message += (message.isEmpty() ? "" : "\n") + Messages.PublisherStepExecution_noResultFor(scan.getScanId());
            }
            scan.setResult(scanResult);
        }
        if (!message.isEmpty()) {
            throw new PublisherStepExecutionError(message);
        }
    }

    /**
     * Break build
     *
     * @throws PublisherStepExecutionError PublisherStepExecutionError
     */
    protected void breakBuild() throws PublisherStepExecutionError {
        if (!breakOptions.isAllowBreakBuild()) {
            return;
        }
        for (Map.Entry<String, PublisherScan> entry : scans.entrySet()) {
            String key = entry.getKey();
            PublisherScan scan = entry.getValue();
            JSONObject scanResult = scan.getResult().getJSONObject("statistics");
            Integer violations, warnings;
            if (breakOptions.isBreakOnVulnerabilities()) {
                violations = scanResult.getJSONObject("vulnerability").getInt("violations");
                warnings = scanResult.getJSONObject("vulnerability").getInt("warnings");
                if (breakOptions.isBreakOnVulnerabilitiesWarningsAndCritical() && (violations > 0 || warnings > 0)) {
                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_vulnerabilities(violations, warnings));
                }
                if (breakOptions.isBreakOnVulnerabilitiesCriticalHitsOnly() && violations > 0) {
                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_vulnerabilities(violations, warnings));
                }
            }
            if (breakOptions.isBreakOnLegalIssues()) {
                violations = scanResult.getJSONObject("legal").getInt("violations");
                warnings = scanResult.getJSONObject("legal").getInt("warnings");
                if (breakOptions.isBreakOnLegalIssuesWarningAndViolations() && (violations > 0 || warnings > 0)) {
                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_legal(violations, warnings));
                }
                if (breakOptions.isBreakOnLegalIssuesViolationsOnly() && violations > 0) {
                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_legal(violations, warnings));
                }
            }
//            if (breakOptions.isBreakOnViabilityIssues()) {
//                violations = scanResult.getJSONObject("viability").getInt("violations");
//                warnings = scanResult.getJSONObject("viability").getInt("warnings");
//                if (breakOptions.isBreakOnViabilityIssuesAll() && (violations > 0 || warnings > 0)) {
//                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_viability(violations, warnings));
//                }
//                if (breakOptions.isBreakOnViabilityIssuesStrongMismatchesOnly() && violations > 0) {
//                    throw new PublisherStepExecutionError(Messages.PublisherStepExecution_viability(violations, warnings));
//                }
//            }
        }
    }

    /**
     * Run publisher step execution
     *
     * @return boolean
     */
    public boolean run() {
        try {
            logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_starting());
            checkCredentials();
            List<Map<String, String>> plugins = getPlugins();
            checkPluginsVersions(plugins);
            runPlugins(plugins);
            getPluginsResults();
            breakBuild();
        } catch (PublisherStepExecutionError e) {
            logger.println(Messages.PublisherStepExecution_loggerLine() + " " + e.getMessage());
            if (breakOptions.isAllowBreakBuild()) {
                build.setResult(Result.FAILURE);
            }
        } finally {
            build.addAction(new PublisherAction(build, scans));
            logger.println(Messages.PublisherStepExecution_loggerLine() + " " + Messages.PublisherStepExecution_finished());
        }
        return build.getResult() != Result.FAILURE;
    }

    /**
     * Constructor
     *
     * @param build        build
     * @param workspace    workspace
     * @param launcher     launcher
     * @param listener     listener
     * @param logger       logger
     * @param project      project
     * @param paths        paths
     * @param credentials  credentials
     * @param breakOptions breakOptions
     */
    PublisherStepExecution(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener, PrintStream logger, String project, ArrayList<PublisherPath> paths, PublisherCredentials credentials, PublisherBreakOptions breakOptions) {
        this.build = build;
        this.workspace = workspace;
        this.launcher = launcher;
        this.listener = listener;
        this.logger = logger;
        this.project = project;
        this.paths = paths;
        this.credentials = credentials;
        this.breakOptions = breakOptions;
        this.client = new RestClient(credentials, this.logger);
    }
}
