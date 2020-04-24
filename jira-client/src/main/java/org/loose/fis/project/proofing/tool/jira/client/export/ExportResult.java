package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExportResult extends GenericJson {
    @Key
    private Map<String, ExportIssueType> issueTypes;
    @Key
    private Map<String, ExportUser> users;
    @Key
    private List<ExportIssue> issues;
}
