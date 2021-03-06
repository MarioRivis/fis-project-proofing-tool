package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.jira.client.JiraPathResolver;
import org.loose.fis.project.proofing.tool.jira.client.JiraService;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;

import java.lang.reflect.Type;
import java.util.List;

public class IssueFieldsService extends JiraService {

	public IssueFieldsService(String jiraHome, HttpRequestInitializer httpRequestInitializer) {
		super(jiraHome, httpRequestInitializer);
	}

	public IssueFieldsService(String jiraHome) {
		super(jiraHome);
	}

	@SneakyThrows
	public List<IssueField> getFields() {
		String apiPath = new JiraPathResolver(jiraHome).getApiPath("field");

		HttpResponse httpResponse = httpClient.get(new GenericUrl(apiPath));
		Type type = new TypeToken<List<IssueField>>() {
		}.getType();
		return (List<IssueField>) httpResponse.parseAs(type);
	}
}
