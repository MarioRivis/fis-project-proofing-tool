package org.loose.fis.project.proofing.tool.github.client.secrets;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretRequestBody {

    @Key
    private String encrypted_value;

    @Key
    private String key_id;
}
