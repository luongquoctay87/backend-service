package vn.tayjava.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;


@Configuration
@Profile({"dev", "test"})
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/backend-service
                .packagesToScan("vn.tayjava.controller")
                .build();
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl) {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
                .info(new Info().title(title)
                        .description("API documents for Backend service")
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }

}
