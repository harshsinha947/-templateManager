package com.templatemanager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "login_mapping")
public class LoginMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "Username", nullable = true)
    private String username;

    @Column(name = "Bot", nullable = true)
    private String bot;

    @Column(name = "bot_id", nullable = true)  // FIXED: Changed from "BotId" to "bot_id"
    private String botId;

    @Column(name = "MappingType", nullable = true)
    private String mappingType;
}
