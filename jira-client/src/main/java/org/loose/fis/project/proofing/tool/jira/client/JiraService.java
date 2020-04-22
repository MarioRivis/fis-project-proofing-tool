package org.loose.fis.project.proofing.tool.jira.client;

import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;
import org.loose.fis.project.proofing.tool.http.HttpClient;

public class JiraService {

    protected String jiraHome;
    protected BasicAuthCredentials credentials;
    protected HttpClient httpClient;

    public JiraService(String jiraHome, BasicAuthCredentials credentials) {
        this.jiraHome = jiraHome;
        this.credentials = credentials;
        this.httpClient = new HttpClient();
    }
}
