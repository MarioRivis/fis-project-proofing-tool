package org.loose.fis.project.proofing.tool.jira.client.issues;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.comments.IssueComment;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.List;

class IssueCommentsServiceTest {
	private static final String JIRA_HOME = "https://inspectorgit.atlassian.net";
	public static final String ISSUE_KEY = "IG-11";

	private final IssueCommentsService issueCommentsService = new IssueCommentsService(JIRA_HOME,
			TestUtils.getJiraCredentials());

	@Test
	void getComments() {
		List<IssueComment> comments = issueCommentsService.getComments(ISSUE_KEY);
		System.out.printf("Received %d comments", comments.size());
	}
}