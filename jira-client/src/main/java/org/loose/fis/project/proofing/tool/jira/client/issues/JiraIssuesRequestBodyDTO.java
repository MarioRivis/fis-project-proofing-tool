package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JiraIssuesRequestBodyDTO {
    @Key
    private String jql;
    @Key
    private int startAt;
    @Key
    private int maxResults;
    @Key
    private List<String> expand;
}
