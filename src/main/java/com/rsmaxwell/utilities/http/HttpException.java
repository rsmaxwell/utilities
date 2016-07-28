package com.rsmaxwell.utilities.http;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@SuppressWarnings("serial")
public class HttpException extends Exception {

    private int responseCode;

    private static Map<Integer, String> lookup = new HashMap<Integer, String>();

    static {
        lookup.put(100, "SC_CONTINUE");
        lookup.put(101, "SC_SWITCHING_PROTOCOLS");
        lookup.put(200, "SC_OK");
        lookup.put(201, "SC_CREATED");
        lookup.put(202, "SC_ACCEPTED");
        lookup.put(203, "SC_NON_AUTHORITATIVE_INFORMATION");
        lookup.put(204, "SC_NO_CONTENT");
        lookup.put(205, "SC_RESET_CONTENT");
        lookup.put(206, "SC_PARTIAL_CONTENT");
        lookup.put(300, "SC_MULTIPLE_CHOICES");
        lookup.put(301, "SC_MOVED_PERMANENTLY");
        lookup.put(302, "SC_MOVED_TEMPORARILY");
        lookup.put(302, "SC_FOUND");
        lookup.put(303, "SC_SEE_OTHER");
        lookup.put(304, "SC_NOT_MODIFIED");
        lookup.put(305, "SC_USE_PROXY");
        lookup.put(307, "SC_TEMPORARY_REDIRECT");
        lookup.put(400, "SC_BAD_REQUEST");
        lookup.put(401, "SC_UNAUTHORIZED");
        lookup.put(402, "SC_PAYMENT_REQUIRED");
        lookup.put(403, "SC_FORBIDDEN");
        lookup.put(404, "SC_NOT_FOUND");
        lookup.put(405, "SC_METHOD_NOT_ALLOWED");
        lookup.put(406, "SC_NOT_ACCEPTABLE");
        lookup.put(407, "SC_PROXY_AUTHENTICATION_REQUIRED");
        lookup.put(408, "SC_REQUEST_TIMEOUT");
        lookup.put(409, "SC_CONFLICT");
        lookup.put(410, "SC_GONE");
        lookup.put(411, "SC_LENGTH_REQUIRED");
        lookup.put(412, "SC_PRECONDITION_FAILED");
        lookup.put(413, "SC_REQUEST_ENTITY_TOO_LARGE");
        lookup.put(414, "SC_REQUEST_URI_TOO_LONG");
        lookup.put(415, "SC_UNSUPPORTED_MEDIA_TYPE");
        lookup.put(416, "SC_REQUESTED_RANGE_NOT_SATISFIABLE");
        lookup.put(417, "SC_EXPECTATION_FAILED");
        lookup.put(500, "SC_INTERNAL_SERVER_ERROR");
        lookup.put(501, "SC_NOT_IMPLEMENTED");
        lookup.put(502, "SC_BAD_GATEWAY");
        lookup.put(503, "SC_SERVICE_UNAVAILABLE");
        lookup.put(504, "SC_GATEWAY_TIMEOUT");
        lookup.put(505, "SC_HTTP_VERSION_NOT_SUPPORTED");
    }

    /**
     *
     */
    public HttpException() {
        //
    }

    /**
     * @param message
     */
    public HttpException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public HttpException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return response code
     */
    public String getResponseCodeText() {
        return lookup.get(responseCode);
    }

    /**
     * @param responseCode
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
