package raiffeisen.ecom.payment.sdk.web;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import raiffeisen.ecom.payment.sdk.model.Response;

import java.io.IOException;
import java.util.Map;

public class ApacheWebClient implements WebClient {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public Response request(String method, String url, Map<String, String> headers, String entity) throws IOException {
        if(method.equals(GET_METHOD)) {
            return getRequest(url, headers);
        }
        else if(method.equals(POST_METHOD)) {
            return postRequest(url, headers, entity);
        }
        else {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    private static Response getRequest(String url, Map<String, String> headers) throws IOException {
        HttpGet getter = new HttpGet(url);
        headers.forEach(getter::addHeader);

        try (CloseableHttpResponse response = httpClient.execute(getter)) {

            int code = response.getStatusLine().getStatusCode();

            return new Response(code, EntityUtils.toString(response.getEntity()));
        }
    }

    private static Response postRequest(String url, Map<String, String> headers, String entity) throws IOException {
        HttpPost poster = new HttpPost(url);
        headers.forEach(poster::addHeader);

        poster.setEntity(new StringEntity(entity));

        try (CloseableHttpResponse response = httpClient.execute(poster)) {

            int code = response.getStatusLine().getStatusCode();

            return new Response(code, EntityUtils.toString(response.getEntity()));
        }
    }
}
