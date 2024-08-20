package com.xi.fmcs.support.util;

import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Data
public class HttpUtil<T> {

    private String url;
    private String body;
    private HttpMethod httpMethod;
    private HttpHeaders header;

    private RestTemplate restTemplate;

    private int connectTimeout = 20;

    public HttpUtil(String url, HttpMethod method) {
        this.url = url;
        body = "{";
        httpMethod = method;
        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(connectTimeout))
                .build();
    }

    private HttpEntity<?> entity;

    public void addHeader(String key, String value) {
        header.add(key, value);
    }

    public void addBody(String key, String value) {
        body += "\"" + key + "\"" + ":\"" + value + "\",";
    }

    public String getBody() {
        return body;
    }

    public void setRequest() {
        entity = new HttpEntity<>(header);
    }

    public void setRequestWithBody() {
        body = body.substring(0, body.length() - 1) + "}";
        entity = new HttpEntity<>(body, header);
    }

    public ResponseEntity<T> getResponse(Class<T> type) {
        ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, entity, type);
        return response;
    }

}
