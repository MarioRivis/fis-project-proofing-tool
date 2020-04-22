package org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.loose.fis.project.proofing.tool.github.client.dto.commons.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFileRequestBody {
    @Key
    private String message;
    @Key
    private String content;
    @Key
    private String branch;
    @Key
    private Author author;
    @Key
    private Author committer;
}
