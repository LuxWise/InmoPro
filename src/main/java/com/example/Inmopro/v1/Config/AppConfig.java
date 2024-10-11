package com.example.Inmopro.v1.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.config")
public class AppConfig {
    private String dbname;

    private String dbuser;

    private String dbpassword;

}
