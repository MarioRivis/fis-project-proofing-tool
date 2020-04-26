package org.loose.fis.project.proofing.tool.github.client;

import org.loose.fis.project.proofing.tool.http.BasicAuthenticationProvider;
import org.loose.fis.project.proofing.tool.http.HttpClient;

public class GithubService {
    protected HttpClient httpClient;

    public GithubService() {
        this.httpClient = new HttpClient();
    }

    public GithubService(BasicAuthenticationProvider authenticationProvider) {
        this.httpClient = new HttpClient(authenticationProvider);
    }
}
