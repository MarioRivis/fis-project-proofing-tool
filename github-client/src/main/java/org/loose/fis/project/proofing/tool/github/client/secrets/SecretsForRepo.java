package org.loose.fis.project.proofing.tool.github.client.secrets;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class SecretsForRepo {

    @Key
    private int total_count;

    @Key
    private List<Secret> secrets;
}
