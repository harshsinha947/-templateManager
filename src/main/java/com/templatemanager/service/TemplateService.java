package com.templatemanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class TemplateService {

    @Value("${template.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Object getTemplateList(String botId) {
        String url = baseUrl + "/api/template/list?botId=" + botId;
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Object.class
            );
            return response.getBody();
        } catch (Exception e) {
            return java.util.Map.of("error", "Failed to fetch templates: " + e.getMessage());
        }
    }
}
