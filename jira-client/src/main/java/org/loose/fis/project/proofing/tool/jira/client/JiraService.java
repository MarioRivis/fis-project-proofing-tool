package org.loose.fis.project.proofing.tool.jira.client;

import com.google.api.client.http.HttpRequestInitializer;
import org.loose.fis.project.proofing.tool.http.HttpClient;

public class JiraService {

    protected String jiraHome;
    protected HttpClient httpClient;

    public JiraService(String jiraHome, HttpRequestInitializer httpRequestInitializer) {
        this.jiraHome = jiraHome;
        this.httpClient = new HttpClient(httpRequestInitializer);
    }

    public JiraService(String jiraHome) {
        this.jiraHome = jiraHome;
        this.httpClient = new HttpClient();
    }
}
