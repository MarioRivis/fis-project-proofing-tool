package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class IssueChange extends GenericJson {
    @Key
    private String id;
    @Key
    private String created;
    @Key
    private List<ChangeItem> items;
}
