package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportIssueType {
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private String description;
    @Key
    private boolean isSubTask;
}
