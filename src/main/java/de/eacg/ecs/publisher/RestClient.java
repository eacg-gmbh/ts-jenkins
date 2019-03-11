package de.eacg.ecs.publisher;

import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.util.Secret;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;

/**
 * Class to do requests to ECS server
 *
 * @author Varanytsia Anatolii
 */
public class RestClient {
    /**
     * Api path
     */
    static private final String apiPath = "/api/v1/";
    /**
     * Credentials
     */
    private final PublisherCredentials credentials;
    /**
     * Logger
     */
    private final PrintStream logger;

    /**
     * Constructor
     *
     * @param credentials credentials
     * @param logger      logger
     */
    RestClient(PublisherCredentials credentials, PrintStream logger) {
        this.credentials = credentials;
        this.logger = logger;
    }

    /**
     * Constructor
     *
     * @param credentials credentials
     */
    RestClient(PublisherCredentials credentials) {
        this.credentials = credentials;
        this.logger = null;
    }

    /**
     * Get scan result
     *
     * @param scanId scanId
     * @return JSONObject
     */
    public JSONObject getScanResult(String scanId) {
        return get("scans/"+ scanId, 8);
    }

    /**
     * Is authorized
     *
     * @return boolean
     */
    public Boolean isAuthorized() {
        return get("authorization") != null;
    }

    /**
     * Get response from path
     *
     * @param path path
     * @return JSONObject
     */
    public JSONObject get(String path) {
        return processRequest(getRequest("get", path));
    }

    /**
     * Get response from path
     *
     * @param path path
     * @param retryCount retryCount
     * @return JSONObject
     */
    public JSONObject get(String path, int retryCount) {
        return repeat("get", path, retryCount);
    }

    /**
     * Repeat
     *
     * @param type get post head put patch trace delete options
     * @param path path
     * @return
     */
    private final JSONObject repeat(String type, String path, int retryCount) {
        JSONObject result = null;
        int tries = 0;
        while (result == null && tries < retryCount) {
            result = processRequest(getRequest(type, path));
            if(result != null){
                return result;
            }
            tries++;
            if (tries < retryCount) {
                logger.println(Messages.RestClient_loggerLine() + " Attempt " + tries + " failed... waiting");
                try {
                    Thread.sleep(1000 * tries);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * Process request
     *
     * @param request request
     * @return JSONObject
     */
    private JSONObject processRequest(Request request) {
        try {
            return jsonToObject(request.execute().returnContent().asString());
            // will it to rewrite it to buffer
        } catch (HttpResponseException e) {
            if (logger != null)
                logger.println(Messages.RestClient_loggerLine() + " " + e.getStatusCode() + " " + e.getMessage());
            return null;
        } catch (IOException e) {
            if (logger != null)
                logger.println(Messages.RestClient_loggerLine() + " " + e.getMessage());
            return null;
        }
    }

    /**
     * Get request
     *
     * @param type get post head put patch trace delete options
     * @param path path
     * @return Request
     */
    private Request getRequest(String type, String path) {
        path = credentials.getUrl() + apiPath + path;
        Request request;
        if (type.equals("head")) {
            request = Request.Head(path);
        } else if (type.equals("post")) {
            request = Request.Post(path);
        } else if (type.equals("patch")) {
            request = Request.Patch(path);
        } else if (type.equals("put")) {
            request = Request.Put(path);
        } else if (type.equals("trace")) {
            request = Request.Trace(path);
        } else if (type.equals("delete")) {
            request = Request.Delete(path);
        } else if (type.equals("options")) {
            request = Request.Options(path);
        } else {
            request = Request.Get(path);
        }
        return request.addHeader("User-Agent", credentials.getUserAgent())
                .addHeader("X-ApiKey", Secret.toString(credentials.getApiToken()))
                .addHeader("X-User", credentials.getUserName())
                .connectTimeout(30000)
                .socketTimeout(30000);
    }

    /**
     * Json is pattern
     *
     * @param json json
     * @return boolean
     */
    private final Boolean jsonIs(String json, String pattern) {
        Pattern mPattern = Pattern.compile(pattern);
        Matcher matcher = mPattern.matcher(json);
        if (matcher.find())
            return true;
        return false;
    }

    /**
     * Json is array
     *
     * @param json json
     * @return boolean
     */
    private final Boolean jsonIsArray(String json) {
        return jsonIs(json, "^\\s*\\[");
    }

    /**
     * Json is Hash
     *
     * @param json json
     * @return boolean
     */
    private final Boolean jsonIsHash(String json) {
        return jsonIs(json, "^\\s*\\{");
    }

    /**
     * Json to object
     *
     * @param json json
     * @return JSONObject
     */
    private final JSONObject jsonToObject(String json) {
        if (jsonIsHash(json)) {
            return JSONObject.fromObject(json);
        } else {
            JSONObject result = new JSONObject();
            result.put("data", jsonIsArray(json) ? JSONArray.fromObject(json) : json);
            return result;
        }
    }
}
