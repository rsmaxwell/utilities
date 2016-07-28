package com.rsmaxwell.utilities.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.rsmaxwell.utilities.basic.Pair;
import com.rsmaxwell.utilities.basic.StringUtilities;

/**
 *
 */
public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private HttpURLConnection connection = null;
    private String path;
    private final List<Pair<String, String>> properties = new ArrayList<Pair<String, String>>();
    private HttpClient client;

    /**
     * @param client
     */
    public HttpRequest(HttpClient client, String path) {
        this.client = client;
        this.path = path;
    }

    /**
     * @return result
     */
    @Override
    public String toString() {
        final UrlBuilder url = new UrlBuilder(client.getEndpoint());
        url.addPath(path);
        return url.toString();
    }

    /**
    *
    */
    public String getPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        return sb.toString();
    }

    /**
     * Send an HTTP request to the server and private int connectTimeout = 0;
     * 
     * @param server
     * @param username
     * @param password
     * @param readTimeout
     * @param connectRetries
     * @param payload
     * @return result
     * @throws IOException
     * @throws HttpException
     */
    public InputStream request(final URL server, final String requestMethod, final Integer connectTimeout, final Integer readTimeout, final Integer connectRetries,
                    final List<Pair<String, String>> properties, final String payload) throws IOException, HttpException {

        InputStream response = null;
        connection = null;
        int retries = 0;
        IOException lastException = null;
        int lastResponseCode = -1;

        while (response == null && retries <= connectRetries) {
            try {
                // Sleep for 1 second between connect retries
                if (retries++ > 0)
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        // Just continue
                    }

                String string = (retries > 1) ? ("attempt " + retries) : "";
                logger.info("HTTP " + requestMethod + " " + server + " " + string);
                connection = (HttpURLConnection) server.openConnection();
                connection.setRequestMethod(requestMethod);

                for (Pair<String, String> pair : properties) {
                    String key = pair.getElement0();
                    String value = pair.getElement1();
                    connection.setRequestProperty(key, value);
                }

                connection.setDoOutput(true);
                setConnectTimeout(connectTimeout);
                setReadTimeout(readTimeout);
                connection.connect();
                sendPayload(payload);
                response = connection.getInputStream();
            }
            catch (final IOException e) {
                lastException = e;
                lastResponseCode = -1;
                if (connection != null)
                    try {
                        lastResponseCode = connection.getResponseCode();
                    }
                    catch (IOException e1) {
                        // Ignore. We just didn't get far enough to get a
                        // response code
                    }
                // Attempt to print any error stream
                // Print the error stream to debug, and re-throw.
                if (connection != null) {
                    try {
                        response = connection.getErrorStream();
                        if (response != null) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(response));
                            String line = br.readLine();
                            while (line != null) {
                                logger.info("* " + line);
                                line = br.readLine();
                            }
                        }
                    }
                    catch (Exception e1) {
                        /* Ignore. We just won't have the extra debug */
                    }
                }

                // If we have a response code, we aren't in connection retry, so
                // Re-throw
                if (lastResponseCode > 0) {
                    logger.info("HTTP request attempt failed with server error (RC=" + lastResponseCode + "). Ret: " + lastException.getMessage());

                    HttpException httpException = new HttpException(lastException);
                    httpException.setResponseCode(lastResponseCode);
                    throw httpException;
                }

                // We are eligible for connection retry.
                logger.info("HTTP request attempt failed (RC=" + lastResponseCode + "): " + lastException.getMessage());
            }
        }

        if (response == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP ");
            sb.append(requestMethod);
            sb.append(" ");
            sb.append(server);
            sb.append(" failed after ");
            sb.append(retries);
            sb.append(" attempts (RC=");
            sb.append(lastResponseCode);
            sb.append("): ");
            sb.append(lastException.getMessage());
            logger.info(sb.toString());

            HttpException httpException = new HttpException(lastException);
            httpException.setResponseCode(lastResponseCode);
            throw httpException;
        }

        return response;
    }

    /**
     * @return response
     * @throws IOException
     */
    public String readResponse(InputStream stream) throws IOException {
        BufferedReader br = null;
        String response = null;
        try {
            final Reader reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(StringUtilities.newline);
                line = br.readLine();
            }
            response = sb.toString();
        }
        finally {
            if (br != null) {
                br.close();
            }
            connection.disconnect();
        }
        return response;
    }

    /**
    * 
    */
    private void setConnectTimeout(final Integer timeout) {
        if (timeout != null) {
            connection.setConnectTimeout(timeout);
        }
    }

    /**
    * 
    */
    private void setReadTimeout(final Integer timeout) {
        if (timeout != null) {
            connection.setReadTimeout(timeout);
        }
    }

    /**
     * @throws IOException
     */
    private void sendPayload(final String payload) throws IOException {
        if (payload != null) {
            final OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(payload);
            wr.flush();
            wr.close();
        }
    }

    /**
     * @return header fields
     */
    public Map<String, List<String>> getHeaderFields() {
        return connection.getHeaderFields();
    }

    /**
     * @return header fields
     */
    public String getHeaderField(String name) {
        return connection.getHeaderField(name);
    }

    /**
     * @param path
     */
    public void addCookie(String key, String value) {
        properties.add(new Pair<String, String>("Cookie", key + "=" + value));
    }

    /**
     * @param connection
     * @param username
     * @param password
     */
    public void authenticate(final String username, final String password) {
        if ((username != null) && ((password != null))) {
            final String userpassword = username + ":" + password;
            String base64Encoded = new String(Base64.encodeBase64(userpassword.getBytes()));

            addProperty("Authorization", "Basic " + base64Encoded);
        }
    }

    /**
     * @param path
     */
    public void addProperty(String key, String value) {
        properties.add(new Pair<String, String>(key, value));
    }

    /**
     * @return header fields
     * @throws Exception
     */
    private Pair<String, Object> parseSetCookie(String string) throws Exception {
        int equals = string.indexOf('=');
        if (equals < 0) {
            throw new Exception("Bad 'Set-Cookie' header: " + string);
        }

        String key = string.substring(0, equals);
        String value = string.substring(equals + 1);
        logger.info("'Set-Cookie': key:" + key + ", value:" + value);
        return new Pair<String, Object>(key, value);
    }

    /**
     * @return header fields
     * @throws Exception
     */
    public String getSetCookie(String name) throws Exception {
        Object value = null;
        String item = connection.getHeaderField("Set-Cookie");
        if (item != null) {
            Pair<String, Object> cookie = parseSetCookie(item);

            if (name.equals(cookie.getElement0())) {
                value = cookie.getElement1();
            }
        }
        return (String) value;
    }

    /**
     * @return header fields
     */
    public List<Pair<String, Object>> getSetCookies() {
        List<Pair<String, Object>> list = new ArrayList<Pair<String, Object>>();

        Map<String, List<String>> headers = connection.getHeaderFields();
        for (String key : headers.keySet()) {
            if ("Set-Cookie".equals(key)) {
                List<String> list2 = headers.get(key);
                for (String string : list2) {
                    try {
                        list.add(parseSetCookie(string));
                    }
                    catch (Exception e) {
                        logger.info(e.getClass().getName() + ":" + e.getMessage());
                    }
                }
            }
            else {
                logger.info("Discarding header: " + key);
            }
        }

        return list;
    }

    /**
     * @return result
     * @throws Exception
     */
    public Object put() throws Exception {

        URL url = new URL(toString());
        Integer connectTimeout = client.getConnectTimeout();
        Integer readTimeout = client.getReadTimeout();
        Integer connectRetries = client.getConnectRetries();
        String payload = null;

        InputStream stream = request(url, "PUT", connectTimeout, readTimeout, connectRetries, properties, payload);
        String response = readResponse(stream);

        return client.checkResponse(response);
    }

    /**
     * @return result
     * @throws Exception
     */
    public Object get() throws Exception {

        URL url = new URL(toString());
        Integer connectTimeout = client.getConnectTimeout();
        Integer readTimeout = client.getReadTimeout();
        Integer connectRetries = client.getConnectRetries();
        String payload = null;

        InputStream stream = request(url, "GET", connectTimeout, readTimeout, connectRetries, properties, payload);
        String response = readResponse(stream);

        return client.checkResponse(response);
    }

    /**
     * @return result
     * @throws Exception
     */
    public Object delete() throws Exception {

        URL url = new URL(toString());
        Integer connectTimeout = client.getConnectTimeout();
        Integer readTimeout = client.getReadTimeout();
        Integer connectRetries = client.getConnectRetries();
        String payload = null;

        InputStream stream = request(url, "DELETE", connectTimeout, readTimeout, connectRetries, properties, payload);
        String response = readResponse(stream);

        return client.checkResponse(response);
    }

    /**
     * @param jsonData
     * @return result
     * @throws Exception
     */
    public Object post(final JsonElement jsonData) throws Exception {

        URL url = new URL(toString());
        Integer connectTimeout = client.getConnectTimeout();
        Integer readTimeout = client.getReadTimeout();
        Integer connectRetries = client.getConnectRetries();
        String payload = jsonData.toString();

        InputStream stream = request(url, "POST", connectTimeout, readTimeout, connectRetries, properties, payload);
        String response = readResponse(stream);

        return client.checkResponse(response);
    }
}
