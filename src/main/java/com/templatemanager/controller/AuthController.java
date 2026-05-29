package com.templatemanager.controller;

import com.templatemanager.repository.LoginMappingRepository;
import com.templatemanager.model.LoginMapping;
import com.templatemanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginMappingRepository loginMappingRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username and password required"));
        }

        Map<String, Object> result = authService.login(username, password);
        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }

    @GetMapping("/bots")
    public ResponseEntity<List<Map<String, String>>> getBots(
            @RequestParam String username,
            @RequestParam String brand) {
        return ResponseEntity.ok(authService.getBotsForBrand(username, brand));
    }

    // DEBUG endpoint — call http://localhost:8080/api/auth/debug-mappings?username=harsh@gmail.com
    @GetMapping("/debug-mappings")
    public ResponseEntity<List<Map<String, Object>>> debugMappings(@RequestParam String username) {
        List<LoginMapping> mappings = loginMappingRepository.findByUsername(username);
        List<Map<String, Object>> result = mappings.stream().map(m -> {
            Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("id",          m.getId());
            row.put("username",    m.getUsername());
            row.put("bot",         m.getBot());
            row.put("botId",       m.getBotId());
            row.put("mappingType", m.getMappingType());
            return row;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
