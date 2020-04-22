package org.loose.fis.project.proofing.tool.github.client.dto.request.repository.refs;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefRequestBody {
    @Key
    private String ref;
    @Key
    private String sha;
}
