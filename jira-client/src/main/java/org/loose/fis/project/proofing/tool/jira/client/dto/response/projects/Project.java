package org.loose.fis.project.proofing.tool.jira.client.dto.response.projects;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Project {

    @Key
    private String key;
    @Key
    private String name;
}
