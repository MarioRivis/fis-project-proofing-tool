package org.loose.fis.project.proofing.tool.jira.client.projects;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.projects.Project;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectsServiceTest {

    private static final String JIRA_HOME = "https://loose.atlassian.net";

    private ProjectsService projectsService;

    @Test
    void getAllProjects() {
        projectsService = new ProjectsService(JIRA_HOME, TestUtils.getJiraCredentials());

        List<Project> projects = projectsService.getAllProjects();

        assertTrue(projects.size() > 0);
    }
}
