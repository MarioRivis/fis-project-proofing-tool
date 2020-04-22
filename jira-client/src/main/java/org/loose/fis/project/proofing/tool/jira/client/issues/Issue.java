package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import lombok.experimental.Delegate;
import org.apache.commons.collections4.CollectionUtils;

@Data
public class Issue extends GenericJson {

    @Key
    private String key;
    @Key
    @Delegate
    private IssueFields fields;

    public boolean hasSubtasks() {
        return CollectionUtils.isNotEmpty(fields.getSubtasks());
    }
}
