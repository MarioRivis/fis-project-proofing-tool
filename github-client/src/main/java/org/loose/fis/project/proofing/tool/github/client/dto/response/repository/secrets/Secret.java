package org.loose.fis.project.proofing.tool.github.client.dto.response.repository.secrets;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Secret extends GenericJson {

    @Key
    private String name;
}
