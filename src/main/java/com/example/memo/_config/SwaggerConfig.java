package com.example.memo._config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(
                new Info()
                .title("OatNote API")
                .description("오트노트의 API 문서")
                .version("v0.0.1")

            )
            .servers(
                List.of(new Server().url("https://oatnote.kro.kr")
            ));

    }
}
