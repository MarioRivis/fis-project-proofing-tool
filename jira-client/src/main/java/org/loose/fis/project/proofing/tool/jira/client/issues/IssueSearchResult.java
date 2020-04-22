package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueSearchResult {
    @Key
    private int startIndex;
    @Key
    private int maxResults;
    @Key
    private int total;
    @Key
    private List<Issue> issues;
}
