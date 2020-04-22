package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class JiraComponent {
    @Key
    private String self;
    @Key
    private String id;
    @Key
    private String name;
    @Key
    private String description;
}