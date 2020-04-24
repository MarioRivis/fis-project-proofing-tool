package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.util.Key;
import lombok.Data;
import org.loose.fis.project.proofing.tool.jira.client.pagination.Paginated;

import java.util.List;

@Data
public class ChangeLog extends Paginated {
    @Key("histories")
    private List<IssueChange> changes;
}
