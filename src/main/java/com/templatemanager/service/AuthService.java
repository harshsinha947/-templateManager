package com.templatemanager.service;

import com.templatemanager.model.LoginMapping;
import com.templatemanager.model.LoginRbm;
import com.templatemanager.repository.LoginMappingRepository;
import com.templatemanager.repository.LoginRbmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private LoginRbmRepository loginRbmRepository;

    @Autowired
    private LoginMappingRepository loginMappingRepository;

    public Map<String, Object> login(String username, String password) {
        Optional<LoginRbm> userOpt = loginRbmRepository.findByUsernameAndPass(username, password);

        if (userOpt.isEmpty()) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("success", false);
            err.put("message", "Invalid email or password");
            return err;
        }

        LoginRbm user = userOpt.get();

        if (user.getStatus() != null && user.getStatus() == 0) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("success", false);
            err.put("message", "Account is inactive. Please contact admin.");
            return err;
        }

        // Fetch bot mappings for this user
        List<LoginMapping> mappings = loginMappingRepository.findByUsername(username);

        // Log for debugging
        System.out.println("=== LOGIN MAPPINGS for [" + username + "] — found: " + mappings.size() + " rows ===");
        for (LoginMapping m : mappings) {
            System.out.println("  id=" + m.getId() + "  bot=" + m.getBot() + "  botId=[" + m.getBotId() + "]  mappingType=" + m.getMappingType());
        }

        // Build brand -> bots structure (null-safe)
        // Key = botName (used as brand), value = list of {botId, botName}
        Map<String, List<Map<String, String>>> brandBotMap = new LinkedHashMap<>();
        for (LoginMapping m : mappings) {
            String botName = (m.getBot()   != null && !m.getBot().trim().isEmpty())   ? m.getBot().trim()   : "Unknown";
            String botId   = (m.getBotId() != null && !m.getBotId().trim().isEmpty()) ? m.getBotId().trim() : "";

            Map<String, String> botEntry = new HashMap<>();
            botEntry.put("botId",   botId);
            botEntry.put("botName", botName);

            // Group by botName as brand key
            brandBotMap.computeIfAbsent(botName, k -> new ArrayList<>()).add(botEntry);
        }

        List<String> brands = new ArrayList<>(brandBotMap.keySet());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success",     true);
        result.put("username",    username);
        result.put("fullname",    user.getFullname() != null ? user.getFullname() : username);
        result.put("userType",    user.getUserType());
        result.put("brands",      brands);
        result.put("brandBotMap", brandBotMap);

        System.out.println("=== brandBotMap returned: " + brandBotMap + " ===");

        return result;
    }

    public List<Map<String, String>> getBotsForBrand(String username, String brand) {
        List<LoginMapping> mappings = loginMappingRepository.findByUsername(username);
        return mappings.stream()
                .filter(m -> brand.equals(m.getBot()))
                .map(m -> {
                    Map<String, String> entry = new HashMap<>();
                    entry.put("botId",   m.getBotId() != null ? m.getBotId().trim() : "");
                    entry.put("botName", m.getBot()   != null ? m.getBot().trim()   : "Unknown");
                    return entry;
                })
                .collect(Collectors.toList());
    }
}