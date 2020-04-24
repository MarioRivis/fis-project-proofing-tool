package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ExportUser {
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private String avatarUrl;
}
