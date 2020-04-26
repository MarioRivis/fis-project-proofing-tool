package org.loose.fis.project.proofing.tool.http;

import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {
    private String username;
    private String password;

    @Override
    public void initialize(HttpRequest httpRequest) {
        httpRequest.setInterceptor(new BasicAuthentication(username, password));
    }
}
