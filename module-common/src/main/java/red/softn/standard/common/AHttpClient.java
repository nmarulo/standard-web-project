package red.softn.standard.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AHttpClient {
    
    private Gson gson;
    
    private String host;
    
    private boolean https;
    
    private String path;
    
    private HttpResponse response;
    
    private boolean returnArray;
    
    protected AHttpClient(String host) {
        this(host, null);
    }
    
    protected AHttpClient(String host, String path) {
        this.gson = GsonUtil.gsonBuilder();
        this.host = DefaultUtils.removeSlash(removeHttp(host));
        this.https = false;
        this.path = path;
        this.response = null;
        this.returnArray = false;
    }
    
    protected <R> R get(Class<R> rClass, String... uri) throws Exception {
        return get(null, rClass, uri);
    }
    
    protected <R, P> R get(P objectParam, Class<?> rClass, String... uri) throws Exception {
        return get(null, objectParam, rClass, uri);
    }
    
    protected <R, O, P> R get(O objectRequest, P objectParam, Class<?> rClass, String... uri) throws Exception {
        return call(Request::Get, objectRequest, rClass, GsonUtil.convertObjectToMap(objectParam), uri);
    }
    
    protected <R, O> R post(O objectRequest, Class<?> rClass, String... uri) throws Exception {
        return call(Request::Post, objectRequest, rClass, null, uri);
    }
    
    protected <R, O> R put(O objectRequest, Class<R> rClass, String... uri) throws Exception {
        return call(Request::Put, objectRequest, rClass, null, uri);
    }
    
    protected void delete(String... uri) throws Exception {
        delete(null, uri);
    }
    
    protected <O> void delete(O objectRequest, String... uri) throws Exception {
        call(Request::Delete, objectRequest, null, null, uri);
    }
    
    protected int getStatusCode() {
        return this.response.getStatusLine()
                            .getStatusCode();
    }
    
    protected abstract void actionResponseException();
    
    protected void setReturnArray(boolean returnArray) {
        this.returnArray = returnArray;
    }
    
    private <R, O> R call(Function<URI, Request> function, O objectRequest, Class<?> rClass, Map<String, Object> parameters, String... uri) throws Exception {
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
    
    private <R> R handleResponse(HttpResponse response, Class<?> rClass) throws IOException {
        this.response = response;
        StatusLine   statusLine = response.getStatusLine();
        HttpEntity   entity     = response.getEntity();
        TypeToken<?> typeToken;
        
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
        
        if (rClass == null) {
            return null;
        }
        
        if (rClass.isArray() && !this.returnArray) {
            typeToken = TypeToken.getParameterized(List.class, rClass.getComponentType());
        } else {
            typeToken = TypeToken.get(rClass);
            this.returnArray = false;
        }
        
        InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
        
        return this.gson.fromJson(inputStreamReader, typeToken.getType());
    }
    
    private void setDefaultHeader(Request request) {
        request.addHeader("Accept", "*/*")
               .addHeader("Content-Type", "application/json; charset=UTF-8")
               .addHeader("Users-Agent", "standard-web-project/0.1.0-ALPHA JAVA/8")
               .addHeader("Accept-Encoding", "gzip, deflate, br")
               .addHeader("Host", "localhost:8888")
               .addHeader("Connection", "keep-alive")
               .addHeader("Cache-Control", "no-cache")
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
            path = Stream.of(this.path, path)
                         .filter(StringUtils::isNotBlank)
                         .collect(Collectors.joining("/"));
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
