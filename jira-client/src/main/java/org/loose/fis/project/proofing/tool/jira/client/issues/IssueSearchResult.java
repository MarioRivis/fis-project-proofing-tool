package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.loose.fis.project.proofing.tool.jira.client.pagination.Paginated;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueSearchResult extends Paginated {
    @Key
    private List<Issue> issues;
}
