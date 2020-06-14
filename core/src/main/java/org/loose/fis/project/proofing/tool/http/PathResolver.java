package org.loose.fis.project.proofing.tool.http;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathResolver {

	private final String apiUrl;

	public PathResolver(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiPath(Map<String, String> variableValues, String... pathVariables) {
		return Stream.concat(Stream.of(apiUrl),
				Arrays.stream(pathVariables).map(var -> replacePlaceholders(var, variableValues)))
				.collect(Collectors.joining("/"));
	}

	@VisibleForTesting
	String replacePlaceholders(String var, Map<String, String> variablesValues) {
		if (var.startsWith(":"))
			return variablesValues.getOrDefault(var.replace(":", ""), var);
		return var;
	}

	public String getApiPath(String... pathVariables) {
		return getApiPath(Collections.emptyMap(), pathVariables);
	}
}
