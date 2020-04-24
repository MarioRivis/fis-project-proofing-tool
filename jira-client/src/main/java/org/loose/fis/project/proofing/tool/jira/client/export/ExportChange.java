package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class ExportChange {
    @Key
    private String id;
    @Key
    private String created;
    @Key
    private ExportUser author;
    @Key
    private List<String> changedFields;
    @Key
    private List<ExportChangeItem> items;
}
