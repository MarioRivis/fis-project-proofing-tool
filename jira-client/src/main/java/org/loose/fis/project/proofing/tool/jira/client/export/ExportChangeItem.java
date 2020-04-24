package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ExportChangeItem {
    @Key
    private String field;
    @Key
    private String fromString;
    @Key
    private String toString;
}
