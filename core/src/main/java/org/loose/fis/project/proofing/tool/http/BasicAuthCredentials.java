package org.loose.fis.project.proofing.tool.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicAuthCredentials {
    private String username;
    private String password;
}
