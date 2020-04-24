package org.loose.fis.project.proofing.tool.utils;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class JsonTestEntity extends GenericJson {
    @Key
    private String id;
    @Key
    private int number;
}
