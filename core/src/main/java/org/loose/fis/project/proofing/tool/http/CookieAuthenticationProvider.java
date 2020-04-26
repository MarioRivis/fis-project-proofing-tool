package org.loose.fis.project.proofing.tool.http;

import com.google.api.client.http.HttpRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CookieAuthenticationProvider implements AuthenticationProvider {
    private String cookie;

    @Override
    public void initialize(HttpRequest httpRequest) {
        httpRequest.getHeaders().setCookie(cookie);
    }
}
