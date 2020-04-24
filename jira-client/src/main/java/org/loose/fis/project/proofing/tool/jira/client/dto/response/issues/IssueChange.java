package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.users.User;

import java.util.List;

@Data
public class IssueChange extends GenericJson {
    @Key
    private String id;
    @Key
    private String created;
    @Key
    private User author;
    @Key
    private List<ChangeItem> items;
}
