package de.eacg.ecs.publisher;

import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final String apiPath = "/api/v1";
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
        int licenses = 10 + (int)(Math.random() * 11);
        int components = 200 + (int)(Math.random() * 101);
        int legal_warnings = (int)(Math.random() * 11);
        int legal_violations =(int)(Math.random() * 4);
        int vulnerability_warnings = (int)(Math.random() * 11);
        int vulnerability_violations = (int)(Math.random() * 4);
        String response = "{\"date\":\"2017-05-25T10:05:59.782Z\",\"scanId\":\"RMTM87L4TB3LyCX2S\",\"components\":[],\"licenses\":[]," +
            "\"statistics\":{\"licenses\":"+licenses+",\"components\":"+components+","+
            "\"legal\":{\"warnings\":"+legal_warnings+",\"violations\":"+legal_violations+"}," +
            "\"vulnerability\":{\"warnings\":"+vulnerability_warnings+",\"violations\":"+vulnerability_violations+"}," +
            "\"viability\":{\"warnings\":68,\"violations\":111}," +
            "\"integrity\":{\"warnings\":0,\"violations\":5}},\"moduleRequirements\":{\"distribution\":[\"LOCAL\"],\"targetCustomer\":[\"ALL\"],\"targetMarket\":[\"GERMANY\"],\"propertyProtection\":\"OPEN_SOURCE\",\"commercialisation\":[\"NON_COMMERCIAL\"]}}";
        return JSONObject.fromObject(response);
    }

    /**
     * Is authorized
     *
     * @return boolean
     */
    public Boolean isAuthorized() {
        return get("/modules") != null;
    }

    /**
     * Get response from path
     *
     * @param path path
     * @return JSONObject
     */
    public JSONObject get(String path) {
        return processRequest(Request.Get(credentials.getUrl() + apiPath + path));
    }

    /**
     * Process request
     *
     * @param request request
     * @return JSONObject
     */
    private JSONObject processRequest(Request request) {
        try {
            return jsonToObject(request.addHeader("User-Agent", credentials.getUserAgent())
                    .addHeader("X-ApiKey", credentials.getApiToken())
                    .addHeader("X-User", credentials.getUserName())
                    .connectTimeout(30000)
                    .socketTimeout(30000)
                    .execute().returnContent().asString());
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
     * Json is array
     *
     * @param json json
     * @return boolean
     */
    private final Boolean jsonIsArray(String json) {
        Pattern mPattern = Pattern.compile("^\\s*\\[");
        Matcher matcher = mPattern.matcher(json);
        if (matcher == null)
            return false;
        if (matcher.find())
            return true;
        return false;
    }

    /**
     * Json to object
     *
     * @param json json
     * @return JSONObject
     */
    private final JSONObject jsonToObject(String json) {
        if (jsonIsArray(json)) {
            JSONObject result = new JSONObject();
            result.put("data", JSONArray.fromObject(json));
            return result;
        } else {
            return JSONObject.fromObject(json);
        }
    }
}
