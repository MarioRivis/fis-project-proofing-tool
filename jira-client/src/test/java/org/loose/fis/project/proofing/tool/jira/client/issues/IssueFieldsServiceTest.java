package org.loose.fis.project.proofing.tool.jira.client.issues;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.List;

class IssueFieldsServiceTest {

	private static final String JIRA_HOME = "https://inspectorgit.atlassian.net";

	private final IssueFieldsService issueFieldsService = new IssueFieldsService(JIRA_HOME,
			TestUtils.getJiraCredentials());

	@Test
	void getAllFields() {
		List<IssueField> fields = issueFieldsService.getFields();
		System.out.println("got " + fields.size() + " fields");
	}
}