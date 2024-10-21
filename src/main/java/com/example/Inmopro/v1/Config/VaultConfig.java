package com.example.Inmopro.v1.Config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VaultConfig {

    @Value("${database}")
    private String database;

    @Value("${dbuser}")
    private String dbuser;

    @Value("${dbpassword}")
    private String dbpassword;

    @PostConstruct
    public void init() {
        if (database == null || dbuser == null || dbpassword == null) {
            log.error("Error: Database configuration incomplete.");
            throw new IllegalArgumentException("Configuration values must be null.");
        }
        log.info("Database configuration its success for the user {}", dbuser);
    }
}
