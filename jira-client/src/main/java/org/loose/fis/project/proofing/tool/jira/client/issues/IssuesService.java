package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;
import org.loose.fis.project.proofing.tool.jira.client.JiraPathResolver;
import org.loose.fis.project.proofing.tool.jira.client.JiraService;
import org.loose.fis.project.proofing.tool.jira.client.pagination.IssueChangelogUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class IssuesService extends JiraService {

    public IssuesService(String jiraHome, BasicAuthCredentials credentials) {
        super(jiraHome, credentials);
    }

    @SneakyThrows
    public List<IssueChange> getChangeLogForIssue(String issueId) {
        return getChangeLogForIssue(issueId, 0);
    }

    @SneakyThrows
    public List<IssueChange> getChangeLogForIssue(String issueKey, int startAt) {
        String apiPath = new JiraPathResolver(jiraHome).getApiPath(ImmutableMap.of("issueId", issueKey), "issue", ":issueId");

        List<IssueChange> allChanges = new ArrayList<>();
        int maxResults = 100;
        int total;

        do {

            HttpResponse httpResponse = httpClient.get(new IssueChangelogUrl(apiPath, startAt, maxResults), credentials);

            Issue issue = httpResponse.parseAs(Issue.class);

            allChanges.addAll(issue.getChangelog().getChanges());

            total = issue.getChangelog().getTotal();
            startAt = startAt + maxResults;
            log.info("Got changes for issue {} ({}/{})", issueKey, Math.min(startAt, total), total);
        } while (startAt < total);

        return allChanges;
    }

    public List<Issue> getAllIssuesForProjects(String... projectKeys) {
        String apiPath = new JiraPathResolver(jiraHome).getApiPath("search");

        List<Issue> allIssues = new ArrayList<>();

        int startAt = 0;
        int maxResults = 500;
        int total;

        String jqlQuery = createJqlQuery(projectKeys);
        do {
            IssueSearchResult searchResult = searchIssues(apiPath, jqlQuery, maxResults, startAt);

            allIssues.addAll(searchResult.getIssues());

            total = searchResult.getTotal();
            startAt = startAt + maxResults;
            log.info("Got issues ({}/{})", Math.min(startAt, total), total);
        } while (startAt < total);

        allIssues.forEach(issue -> {
            if (issue.getChangelog().getMaxResults() < issue.getChangelog().getTotal()) {
                issue.getChangelog().getChanges().addAll(getChangeLogForIssue(issue.getKey(), issue.getChangelog().getMaxResults()));
            }
        });

        return allIssues;
    }

    @SneakyThrows
    private IssueSearchResult searchIssues(String apiPath, String jqlQuery, int maxResults, int startAt) {
        HttpResponse httpResponse = httpClient.post(new GenericUrl(apiPath), credentials,
                new JiraIssuesRequestBodyDTO(jqlQuery, startAt, maxResults, Arrays.asList("changelog")));
        return httpResponse.parseAs(IssueSearchResult.class);
    }

    private String createJqlQuery(String... existingJiraProjects) {
        String jql = "project in (";
        jql += Arrays.stream(existingJiraProjects)
                .map(this::encloseInQuotes)
                .collect(Collectors.joining(","));
        jql += ")";

        return jql;
    }

    private String encloseInQuotes(String s) {
        return String.format("\"%s\"", s);
    }

}
