package com.algosheets.backend.config.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException, IOException {
        logRequestDetails(request);
        ClientHttpResponse response = execution.execute(request, body);
        logResponseDetails(response);
        return response;
    }

    private void logRequestDetails(HttpRequest request) throws IOException {
        log.info("URI: {}", request.getURI());
        log.info("Method: {}", request.getMethod());
        log.info("Headers: {}", request.getHeaders());
    }

    private void logResponseDetails(ClientHttpResponse response) throws IOException {
        log.info("Status code: {}", response.getStatusCode());
        log.info("Status text: {}", response.getStatusText());
        log.info("Headers: {}", response.getHeaders());
    }
}