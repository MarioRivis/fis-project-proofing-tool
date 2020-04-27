package org.loose.fis.project.proofing.tool.jira.client;

import org.loose.fis.project.proofing.tool.http.PathResolver;

import static java.lang.String.join;

public class JiraPathResolver extends PathResolver {

    private static final String JIRA_REST_API_PATH = "rest/api";

    public JiraPathResolver(String jiraHome) {
        this(jiraHome, "3");
    }

    public JiraPathResolver(String jiraHome, String apiVersion) {
        super(getApiUrl(jiraHome, apiVersion));
    }

    private static String getApiUrl(String jiraHome, String apiVersion) {
        return join("/", jiraHome, JIRA_REST_API_PATH, apiVersion);
    }
}
