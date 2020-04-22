package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class IssueType {
    @Key
    private String name;
    @Key
    private String description;
    @Key("subtask")
    private boolean isSubTask;
}
