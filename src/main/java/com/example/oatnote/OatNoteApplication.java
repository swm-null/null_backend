package com.example.oatnote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.oatnote._commons.config")
@EnableRetry
public class OatNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(OatNoteApplication.class, args);
    }
}
