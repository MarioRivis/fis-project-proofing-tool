package org.loose.fis.project.proofing.tool.jira.client;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JiraPathResolverTest {

    private static final String JIRA_HOME = "https://loose.atlassian.net";
    JiraPathResolver jiraPathResolver;

    @Test
    void getApiPath() {
        jiraPathResolver = new JiraPathResolver(JIRA_HOME);
        Map<String, String> map = ImmutableMap.of("projectKey", "SM");

        assertEquals("https://loose.atlassian.net/rest/api/2/project/SM/properties", jiraPathResolver.getApiPath(map, "project", ":projectKey", "properties"));
    }
}