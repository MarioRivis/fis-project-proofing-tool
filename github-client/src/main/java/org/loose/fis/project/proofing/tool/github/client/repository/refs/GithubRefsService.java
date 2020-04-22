package org.loose.fis.project.proofing.tool.github.client.repository.refs;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.refs.RefRequestBody;
import org.loose.fis.project.proofing.tool.github.client.dto.response.repository.refs.Ref;
import org.loose.fis.project.proofing.tool.github.client.repository.GithubRepositoryService;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;

public class GithubRefsService extends GithubRepositoryService {

    public GithubRefsService(String owner, String repo) {
        super(owner, repo);
    }

    public GithubRefsService(String owner, String repo, BasicAuthCredentials credentials) {
        super(owner, repo, credentials);
    }

    @SneakyThrows
    public Ref getRef(String refName) {
        String apiPath = getApiPath(ImmutableMap.of("ref", refName), "git", "ref", ":ref");

        HttpResponse httpResponse = httpClient.get(new GenericUrl(apiPath), credentials);

        return httpResponse.parseAs(Ref.class);
    }

    public Ref getBranch(String branchName) {
        return getRef("heads/" + branchName);
    }

    public Ref getTag(String tagName) {
        return getRef("tags/" + tagName);
    }

    @SneakyThrows
    public Ref createRef(String refName, String sha) {
        String apiPath = getApiPath("git", "refs");

        RefRequestBody body = new RefRequestBody(refName, sha);

        HttpResponse httpResponse = httpClient.post(new GenericUrl(apiPath), credentials, body);

        return httpResponse.parseAs(Ref.class);
    }

    public Ref createBranch(String branchName) {
        return getRef("refs/heads/" + branchName);
    }

    public Ref createTag(String tagName) {
        return getRef("refs/tags/" + tagName);
    }

}
