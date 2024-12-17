package com.consoleolog.anyaskapiserver.v1.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SerperSearchTool {
    private static final String SERPER_API_HEADER = "X-API-KEY";
    private static final String URL = "https://google.serper.dev/search";

    @Value("${serper.api.key}")
    private String SERPER_API_KEY;

    public Map<String, Object> search(String query){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(SERPER_API_HEADER, SERPER_API_KEY);

        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("q", query);

        HttpEntity<LinkedHashMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>(){}
        );
        return response.getBody();
    }

    public List<String> getLinks(Map<String, Object> attributes){

        List<String> links = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> organic = (List<Map<String, Object>>) attributes.get("organic");

        for (Map<String, Object> result: organic){
            links.add((String) result.get("link"));
        }

        return links;
    }

}
