package org.loose.fis.project.proofing.tool.github.client.dto.response.invitations;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Invitation extends GenericJson {
    @Key
    private BigDecimal id;
    @Key
    private GenericJson repository;
    @Key
    private String permissions;

    public String getRepoUrl() {
        return (String) getRepository().get("html_url");
    }
}
