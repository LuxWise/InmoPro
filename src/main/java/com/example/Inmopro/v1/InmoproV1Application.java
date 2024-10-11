package com.example.Inmopro.v1;

import com.example.Inmopro.v1.Config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class InmoproV1Application  implements CommandLineRunner {

	@Autowired
	private AppConfig appConfig;

	public static void main(String[] args) {
		SpringApplication.run(InmoproV1Application.class, args);
	}

	@Override
	public void  run(String... args) throws Exception {
		log.info("Application started");
	}
}
