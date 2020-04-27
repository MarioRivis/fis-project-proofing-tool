package org.loose.fis.project.proofing.tool.http;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@NoArgsConstructor
public class HttpClient {

    private static HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();

    private HttpRequestInitializer httpRequestInitializer;

    @SneakyThrows
    public HttpResponse get(GenericUrl url) {
        HttpRequestFactory requestFactory = getHttpRequestFactory();

        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse patch(GenericUrl url, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory();

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPatchRequest(url, content);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse post(GenericUrl url) {
        return post(url, null);
    }

    @SneakyThrows
    public HttpResponse post(GenericUrl url, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory();

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPostRequest(url, content);
        return request.execute();
    }

    @SneakyThrows
    public HttpResponse put(GenericUrl url, Object body) {
        HttpRequestFactory requestFactory = getHttpRequestFactory();

        JsonHttpContent content = getJsonHttpContent(body);

        HttpRequest request = requestFactory.buildPutRequest(url, content);
        request.setLoggingEnabled(true);
        return request.execute();
    }

    private JsonHttpContent getJsonHttpContent(Object body) {
        JsonHttpContent content = null;
        if (body != null)
            content = new JsonHttpContent(new JacksonFactory(), body);
        return content;
    }

    private HttpRequestFactory getHttpRequestFactory() {
        return HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                    if (httpRequestInitializer != null)
                        httpRequestInitializer.initialize(request);
                });
    }
}
