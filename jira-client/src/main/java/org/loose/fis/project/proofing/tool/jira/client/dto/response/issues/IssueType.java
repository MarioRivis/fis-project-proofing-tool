package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class IssueType {
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private String description;
    @Key("subtask")
    private boolean isSubTask;
}
