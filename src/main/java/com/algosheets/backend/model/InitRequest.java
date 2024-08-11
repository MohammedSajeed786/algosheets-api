package com.algosheets.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitRequest<T> {
    private String url;
    private Object body;
    private Map<String,String> headers;
    private Class<T> responseType;
    private HttpMethod method;
}
