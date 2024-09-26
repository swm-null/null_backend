package com.example.oatnote._config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    private final String serverUrl;

    public SwaggerConfig(
        @Value("${swagger.server.url}") String serverUrl
    ) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .openapi("3.1.0")
            .info(apiInfo())
            .addServersItem(apiServer())
            .components(jwtComponents())
            .addSecurityItem(apiSecurityRequirement());
    }

    private Info apiInfo() {
        return new Info()
            .title("OatNote API")
            .description("오트노트의 API 문서입니다.")
            .version("v0.0.4");
    }

    private Server apiServer() {
        return new Server()
            .url(serverUrl);
    }

    private SecurityRequirement apiSecurityRequirement() {
        String jwt = "Jwt Authentication";
        return new SecurityRequirement().addList(jwt);
    }

    private Components jwtComponents() {
        String jwt = "Jwt Authentication";
        return new Components().addSecuritySchemes(jwt, new SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .description("토큰값을 입력하여 인증을 활성화할 수 있습니다.")
            .bearerFormat("JWT")
        );
    }
}
