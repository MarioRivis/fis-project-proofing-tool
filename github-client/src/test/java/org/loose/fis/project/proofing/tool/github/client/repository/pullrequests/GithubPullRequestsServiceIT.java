package org.loose.fis.project.proofing.tool.github.client.repository.pullrequests;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.pullrequests.CreatePullRequestBody;
import org.loose.fis.project.proofing.tool.github.client.dto.response.repository.pullrequests.PullRequest;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GithubPullRequestsServiceIT {

    private GithubPullRequestsService service = new GithubPullRequestsService("gogmaster", "SimpleRegistrationExample", TestUtils.getGithubCredentials());

    @Test
    void testCreatePullRequest() {
        String prTitle = "Created Build Workflow";
        String prBody = "Added a workflow to build your application on every Pull Request to Master and on every push to the master branch!" +
                " The workflow should run now, because of this PR :)";
        CreatePullRequestBody body = CreatePullRequestBody.builder()
                .title(prTitle)
                .body(prBody)
                .base("master")
                .head("actions")
                .draft(false)
                .build();

        PullRequest pullRequest = service.createPullRequest(body);

        assertNotNull(pullRequest);
        assertEquals(prTitle, pullRequest.getTitle());
        assertEquals(prBody, pullRequest.getBody());
        assertEquals("open", pullRequest.getState());
    }
}