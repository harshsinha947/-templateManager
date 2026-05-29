package com.templatemanager.controller;

import com.templatemanager.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("/list")
    public ResponseEntity<Object> getTemplates(@RequestParam String botId) {
        Object result = templateService.getTemplateList(botId);
        return ResponseEntity.ok(result);
    }
}
