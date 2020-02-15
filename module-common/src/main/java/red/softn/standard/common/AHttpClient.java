package red.softn.standard.common;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AHttpClient {
    
    private Gson gson;
    
    private String host;
    
    private boolean https;
    
    private String path;
    
    private HttpResponse response;
    
    public AHttpClient(String host) {
        this(host, null);
    }
    
    public AHttpClient(String host, String path) {
        this.gson = GsonUtil.gsonBuilder();
        this.host = DefaultUtils.removeSlash(removeHttp(host));
        this.https = false;
        this.path = path;
        this.response = null;
    }
    
    public <R> R get(Class<R> rClass, String... uri) throws Exception {
        return get(null, rClass, uri);
    }
    
    public <R, O> R get(O objectRequest, Class<R> rClass, String... uri) throws Exception {
        return call(Request::Get, null, rClass, GsonUtil.convertObjectToMap(objectRequest), uri);
    }
    
    public <R, O> R post(O objectRequest, Class<R> rClass, String... uri) throws Exception {
        return call(Request::Post, objectRequest, rClass, null, uri);
    }
    
    public <R, O> R put(O objectRequest, Class<R> rClass, String... uri) throws Exception {
        return call(Request::Put, objectRequest, rClass, null, uri);
    }
    
    public <O> void delete(O objectRequest, String... uri) throws Exception {
        call(Request::Delete, objectRequest, null, null, uri);
    }
    
    public int getStatusCode() {
        return this.response.getStatusLine()
                            .getStatusCode();
    }
    
    protected abstract void actionResponseException();
    
    private <R, O> R call(Function<URI, Request> function, O objectRequest, Class<R> rClass, Map<String, Object> parameters, String... uri) throws Exception {
        try {
            Request request = function.apply(uriBuilder(parameters, uri));
            setBody(objectRequest, request);
            setDefaultHeader(request);
            
            return request.execute()
                          .handleResponse(response -> handleResponse(response, rClass));
        } catch (Exception ex) {
            actionResponseException();
            
            throw ex;
        }
    }
    
    private <R> R handleResponse(HttpResponse response, Class<R> rClass) throws IOException {
        this.response = response;
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity     = response.getEntity();
        
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
        
        if (rClass == null) {
            return null;
        }
        
        return GsonUtil.gsonBuilder()
                       .fromJson(new InputStreamReader(entity.getContent()), rClass);
    }
    
    private void setDefaultHeader(Request request) {
        request.addHeader("Accept", "application/json")
               .addHeader("Content-Type", "application/json; charset=UTF-8")
               .addHeader("Users-Agent", "standard-web-project/0.1.0-ALPHA JAVA/8")
               .addHeader("Accept-Encoding", "")
               .addHeader("Expect", "");
    }
    
    private <O> void setBody(O objectRequest, Request request) {
        if (objectRequest != null) {
            request.bodyString(this.gson.toJson(objectRequest), ContentType.APPLICATION_JSON);
        }
    }
    
    private URI uriBuilder(Map<String, Object> parameters, String... uri) throws URISyntaxException {
        String path = Arrays.stream(uri)
                            .filter(StringUtils::isNotBlank)
                            .map(DefaultUtils::removeSlash)
                            .collect(Collectors.joining("/"));
        
        if (StringUtils.isNotBlank(this.path)) {
            path = String.join("/", this.path, path);
        }
        
        URIBuilder uriBuilder = new URIBuilder().setScheme(this.https ? "https" : "http")
                                                .setHost(this.host)
                                                .setPath(path);
        
        if (!DefaultUtils.isEmpty(parameters)) {
            parameters.forEach((key, value) -> uriBuilder.setParameter(key, String.valueOf(value)));
            
        }
        
        return uriBuilder.build();
    }
    
    private String removeHttp(String str) {
        return StringUtils.removeStartIgnoreCase(StringUtils.removeStartIgnoreCase(str, "https://"), "http://");
    }
}
