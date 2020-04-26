package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.jira.client.JiraPathResolver;
import org.loose.fis.project.proofing.tool.jira.client.JiraService;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.comments.CommentsSearchResult;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.comments.IssueComment;

import java.util.List;

public class IssueCommentsService extends JiraService {

	public static final String API_VERSION = "2";

	public IssueCommentsService(String jiraHome, HttpRequestInitializer httpRequestInitializer) {
		super(jiraHome, httpRequestInitializer);
	}

	public IssueCommentsService(String jiraHome) {
		super(jiraHome);
	}


	@SneakyThrows
	public List<IssueComment> getComments(String issueKey) {
		String apiPath = new JiraPathResolver(jiraHome, API_VERSION).getApiPath("issue", issueKey, "comment");
		HttpResponse httpResponse = httpClient.get(new GenericUrl(apiPath));

		return httpResponse.parseAs(CommentsSearchResult.class).getComments();
	}

	public void addCommentsToIssue(Issue issue) {
		issue.setComments(getComments(issue.getKey()));
	}
}
