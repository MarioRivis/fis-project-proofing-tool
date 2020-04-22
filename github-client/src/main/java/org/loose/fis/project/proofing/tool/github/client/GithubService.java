package org.loose.fis.project.proofing.tool.github.client;

import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;
import org.loose.fis.project.proofing.tool.http.HttpClient;

public class GithubService {
    protected HttpClient httpClient;
    protected BasicAuthCredentials credentials;

    public GithubService() {
        this.httpClient = new HttpClient();
    }

    public GithubService(BasicAuthCredentials credentials) {
        this.credentials = credentials;
        this.httpClient = new HttpClient();
    }
}
