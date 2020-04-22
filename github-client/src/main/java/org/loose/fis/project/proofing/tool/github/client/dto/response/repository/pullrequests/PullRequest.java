package org.loose.fis.project.proofing.tool.github.client.dto.response.repository.pullrequests;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class PullRequest extends GenericJson {
    @Key
    private String state;
    @Key
    private String title;
    @Key
    private String body;
}
