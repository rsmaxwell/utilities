package com.rsmaxwell.utilities.http;

public interface HttpClient {

    int getConnectTimeout();

    int getReadTimeout();

    int getConnectRetries();

    String getEndpoint();

    Object checkResponse(String response) throws Exception;
}
