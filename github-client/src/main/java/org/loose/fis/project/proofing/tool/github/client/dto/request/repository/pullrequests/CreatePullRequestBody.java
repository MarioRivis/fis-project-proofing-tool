package org.loose.fis.project.proofing.tool.github.client.dto.request.repository.pullrequests;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePullRequestBody {
    @Key
    private String title;
    @Key
    private String head;
    @Key
    private String base;
    @Key
    private String body;
    @Key("maintainer_can_modify")
    private boolean maintainerCanModify;
    @Key
    private boolean draft;
}
