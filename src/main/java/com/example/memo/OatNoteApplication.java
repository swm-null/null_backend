package com.example.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.memo._config")
public class OatNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(OatNoteApplication.class, args);
    }
}
