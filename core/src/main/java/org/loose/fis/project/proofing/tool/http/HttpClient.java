package org.loose.fis.project.proofing.tool.http;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.SneakyThrows;

public class HttpClient {

    private static HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();


    @SneakyThrows
    public HttpResponse get(GenericUrl url, BasicAuthCredentials credentials) {
        HttpRequestFactory requestFactory = getHttpRequestFactory(credentials);

        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse patch(GenericUrl url, BasicAuthCredentials credentials, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory(credentials);

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPatchRequest(url, content);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse post(GenericUrl url, BasicAuthCredentials credentials, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory(credentials);

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPostRequest(url, content);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse put(GenericUrl url, BasicAuthCredentials credentials, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory(credentials);

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPutRequest(url, content);
        return request.execute();
    }

    private JsonHttpContent getJsonHttpContent(Object body) {
        JsonHttpContent content = null;
        if (body != null)
            content = new JsonHttpContent(new JacksonFactory(), body);
        return content;
    }

    private HttpRequestFactory getHttpRequestFactory(BasicAuthCredentials credentials) {
        return HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                    if (credentials != null)
                        request.setInterceptor(new BasicAuthentication(credentials.getUsername(), credentials.getPassword()));
                });
    }
}
