package org.loose.fis.project.proofing.tool.jira.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JiraPathResolver {

    private static String JIRA_REST_API_PATH = "rest/api/2";

    private String jiraHome;

    public JiraPathResolver(String jiraHome) {
        this.jiraHome = jiraHome;
    }

    public String getApiPath(Map<String, String> variableValues, String... pathVariables) {
        return Stream.concat(Stream.of(jiraHome, JIRA_REST_API_PATH), Arrays.stream(pathVariables).map(var -> replacePlaceholders(var, variableValues)))
                .collect(Collectors.joining("/"));
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
