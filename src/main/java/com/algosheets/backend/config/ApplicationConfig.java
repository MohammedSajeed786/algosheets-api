package com.algosheets.backend.config;

import com.algosheets.backend.config.interceptors.LoggingRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
