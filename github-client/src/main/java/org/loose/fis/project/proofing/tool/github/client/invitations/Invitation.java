package org.loose.fis.project.proofing.tool.github.client.invitations;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Invitation extends GenericJson {

    @Key
    private BigDecimal id;

}
