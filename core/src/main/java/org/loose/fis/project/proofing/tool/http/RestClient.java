package org.loose.fis.project.proofing.tool.http;

import com.google.api.client.http.HttpRequestInitializer;

public abstract class RestClient {

    protected HttpClient httpClient;
    protected PathResolver pathResolver;

    public RestClient() {
        this.httpClient = new HttpClient();
        this.pathResolver = new PathResolver(getApiUrl());
    }

    public RestClient(HttpRequestInitializer httpRequestInitializer) {
        this.httpClient = new HttpClient(httpRequestInitializer);
        this.pathResolver = new PathResolver(getApiUrl());
    }

    protected abstract String getApiUrl();

}
