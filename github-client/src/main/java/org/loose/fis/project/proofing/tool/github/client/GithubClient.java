package org.loose.fis.project.proofing.tool.github.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GithubClient {

    private static final String GITHUB_API_PATH = "https://api.github.com";

    public static String getApiPath(Map<String, String> variablesValues, String... pathVariables) {
        return Stream.concat(Stream.of(GITHUB_API_PATH), Arrays.stream(pathVariables).map(var -> replacePlaceholders(var, variablesValues))).collect(Collectors.joining("/"));
    }

    static String replacePlaceholders(String var, Map<String, String> variablesValues) {
        if (var.startsWith(":"))
            return variablesValues.getOrDefault(var.replace(":", ""), var);
        return var;
    }

    public static String getApiPath(String... pathVariables) {
        return getApiPath(Collections.emptyMap(), pathVariables);
    }
}
