package org.loose.fis.project.proofing.tool.jira.client.projects;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.jira.client.JiraPathResolver;
import org.loose.fis.project.proofing.tool.jira.client.JiraService;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.projects.Project;

import java.lang.reflect.Type;
import java.util.List;

public class ProjectsService extends JiraService {

    public ProjectsService(String jiraHome, HttpRequestInitializer httpRequestInitializer) {
        super(jiraHome, httpRequestInitializer);
    }

    public ProjectsService(String jiraHome) {
        super(jiraHome);
    }

    @SneakyThrows
    public List<Project> getAllProjects() {

        String apiPath = new JiraPathResolver(jiraHome).getApiPath("project");

        HttpResponse httpResponse = httpClient.get(new GenericUrl(apiPath));

        Type type = new TypeToken<List<Project>>() {
        }.getType();

        return (List<Project>) httpResponse.parseAs(type);
    }

    @SneakyThrows
    public Project getProject(String projectKey) {

        String apiPath = new JiraPathResolver(jiraHome).getApiPath(ImmutableMap.of("projectKey", projectKey), "project", ":projectKey");

        HttpResponse httpResponse = httpClient.get(new GenericUrl(apiPath));

        return httpResponse.parseAs(Project.class);
    }

}
