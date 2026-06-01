package com.templatemanager.controller;

import com.templatemanager.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Value("${template.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/list")
    public ResponseEntity<Object> getTemplates(@RequestParam String botId) {
        Object result = templateService.getTemplateList(botId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/image")
    public ResponseEntity<String> proxyImage(@RequestParam String fileName) {
        String[] urls = {
                baseUrl + "/api/template/image?fileName=" + fileName,
                baseUrl + "/images/" + fileName,
                baseUrl + "/uploads/" + fileName,
                baseUrl + "/" + fileName
        };
        for (String url : urls) {
            try {
                ResponseEntity<byte[]> resp = restTemplate.exchange(
                        url, HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()), byte[].class
                );
                if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                    String ct = resp.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
                    if (ct == null) ct = "image/jpeg";
                    String b64 = "data:" + ct + ";base64," + Base64.getEncoder().encodeToString(resp.getBody());
                    return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/plain").body(b64);
                }
            } catch (Exception ignored) {}
        }
        return ResponseEntity.notFound().build();
    }
}