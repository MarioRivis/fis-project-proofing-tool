package org.loose.fis.project.proofing.tool.jira.client.issues;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IssuesServiceTest {

    private static final String JIRA_HOME = "https://loose.atlassian.net";

    private IssuesService issuesService = new IssuesService(JIRA_HOME, TestUtils.getJiraCredentials());

    @Test
    void getAllIssuesForProjects() {
        List<Issue> issues = issuesService.getAllIssuesForProjects("SM");

        assertTrue(issues.size() >= 25);


        Map<String, Issue> keyToIssue = issues.stream().collect(Collectors.toMap(Issue::getKey, Function.identity()));

        double averageSubtasksPerStory = issues.stream()
                .filter(issue -> !issue.getIssuetype().isSubTask())
                .sorted(Comparator.comparing(issue -> issue.getSubtasks().size()))
                .peek(issue -> System.out.println(String.format("[%d] - [%s]: %s %s", issue.getSubtasks().size(), issue.getIssuetype().getName().toUpperCase().charAt(0), issue.getKey(), issue.getSummary())))
                .mapToInt(issue -> issue.getSubtasks().size())
                .average().getAsDouble();

        System.out.printf("Average subtasks per story: %f", averageSubtasksPerStory);

    }
}