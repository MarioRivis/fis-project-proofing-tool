package org.loose.fis.project.proofing.tool.github.client.dto.commons;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Author {
    @Key
    private String name;
    @Key
    private String email;
}
