package com.example.tusori_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String accessToken = "AccessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(accessToken);
        Components components = new Components().addSecuritySchemes(accessToken, new SecurityScheme()
                .name(accessToken)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
                .info(apiInfo())
                .components(components)
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("Swagger")
                .description("Tusori(투설이)")
                .version("1.0.0");
    }
}
