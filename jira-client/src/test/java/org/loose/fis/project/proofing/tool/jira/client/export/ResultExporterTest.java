package org.loose.fis.project.proofing.tool.jira.client.export;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssueFieldsService;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssuesService;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class ResultExporterTest {

	private static final String JIRA_HOME = "https://inspectorgit.atlassian.net";

	private final IssuesService issuesService = new IssuesService(JIRA_HOME, TestUtils.getJiraCredentials());

	private final IssueFieldsService issueFieldsService = new IssueFieldsService(JIRA_HOME,
			TestUtils.getJiraCredentials());

	@Test
	void exportIssues() {
		List<Issue> issues = issuesService.getAllIssuesForProjects("IG");
		List<IssueField> issueFields = issueFieldsService.getFields();

		List<IssueField> testCustomFields = issueFields.stream()
				.filter(issueField -> issueField.getId().equals("customfield_10026") || issueField.getId()
						.equals("customfield_10018")).collect(Collectors.toList());
		new ResultExporter().export(issues, Paths.get("./tasks.json"), testCustomFields);
	}

}