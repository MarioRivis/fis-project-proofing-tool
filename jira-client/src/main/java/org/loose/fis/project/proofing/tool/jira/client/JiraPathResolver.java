package org.loose.fis.project.proofing.tool.jira.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JiraPathResolver {

    private static final String JIRA_REST_API_PATH = "rest/api/";

    private final String jiraHome;
    private final String apiVersion;

    public JiraPathResolver(String jiraHome) {
        this(jiraHome, "3");
    }

    public JiraPathResolver(String jiraHome, String apiVersion) {
        this.jiraHome = jiraHome;
        this.apiVersion = apiVersion;
    }

    public String getApiPath(Map<String, String> variableValues, String... pathVariables) {
        return Stream.concat(Stream.of(jiraHome, getApiPath()),
                Arrays.stream(pathVariables).map(var -> replacePlaceholders(var, variableValues)))
                .collect(Collectors.joining("/"));
    }

    private String getApiPath() {
        return JIRA_REST_API_PATH + apiVersion;
    }

    String replacePlaceholders(String var, Map<String, String> variablesValues) {
        if (var.startsWith(":"))
            return variablesValues.getOrDefault(var.replace(":", ""), var);
        return var;
    }

    public String getApiPath(String... pathVariables) {
        return getApiPath(Collections.emptyMap(), pathVariables);
    }
}
