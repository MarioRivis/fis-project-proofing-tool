package org.loose.fis.project.proofing.tool.github.client.repository.contents;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents.CreateFileRequestBody;
import org.loose.fis.project.proofing.tool.github.client.repository.GithubRepositoryService;
import org.loose.fis.project.proofing.tool.http.BasicAuthenticationProvider;

public class GithubContentsService extends GithubRepositoryService {

    public GithubContentsService(String owner, String repo) {
        super(owner, repo);
    }

    public GithubContentsService(String owner, String repo, BasicAuthenticationProvider authenticationProvider) {
        super(owner, repo, authenticationProvider);
    }

    public boolean addFileToRepo(String path, CreateFileRequestBody body) {
        String apiPath = getApiPath(ImmutableMap.of("path", path), "contents", ":path");

        HttpResponse httpResponse = httpClient.put(new GenericUrl(apiPath), body);

        int statusCode = httpResponse.getStatusCode();
        return statusCode == 200 || statusCode == 201;
    }

}
