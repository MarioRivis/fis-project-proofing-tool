package org.loose.fis.project.proofing.tool.github.client.repository;

import com.google.common.collect.ImmutableMap;
import org.loose.fis.project.proofing.tool.github.client.GithubClient;
import org.loose.fis.project.proofing.tool.github.client.GithubService;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GithubRepositoryService extends GithubService {

    protected String owner;
    protected String repo;

    private ImmutableMap<String, String> map;

    public GithubRepositoryService(String owner, String repo) {
        super();
        this.owner = owner;
        this.repo = repo;

        initMap(owner, repo);
    }

    public GithubRepositoryService(String owner, String repo, BasicAuthCredentials credentials) {
        super(credentials);
        this.owner = owner;
        this.repo = repo;

        initMap(owner, repo);
    }

    private void initMap(String owner, String repo) {
        map = ImmutableMap.of("repo", repo, "owner", owner);
    }

    protected String getApiPath(Map<String, String> variablesValues, String... pathVariables) {
        Map<String, String> newMap = new HashMap<>(variablesValues);
        newMap.putAll(map);

        return GithubClient.getApiPath(newMap,
                Stream.concat(Stream.of("repos", ":owner", ":repo"), Arrays.stream(pathVariables)).toArray(String[]::new));
    }

    protected String getApiPath(String... pathVariables) {
        return GithubClient.getApiPath(map,
                Stream.concat(Stream.of("repos", ":owner", ":repo"), Arrays.stream(pathVariables)).toArray(String[]::new));
    }
}
