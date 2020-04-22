package org.loose.fis.project.proofing.tool.jira.client.issues;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JiraIssuesRequestBodyDTO {
    private String jql;
    private int startAt;
    private int maxResults;
}