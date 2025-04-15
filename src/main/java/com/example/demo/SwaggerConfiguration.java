package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {
    private static final String JWT = "JWT";

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(JWT))
            .components(new Components().addSecuritySchemes(JWT, createAPIKeyScheme()))
            .info(new Info().title("WMS API 문서")
                .version("1.0.0")
                .description("WMS Service API 문서입니다."));
    }

    @Bean
    public OpenApiCustomizer globalResponseCustomizer() {
        return openApi -> openApi.getPaths()
            .values()
            .forEach(pathItem ->
                pathItem.readOperations()
                    .forEach(operation -> {
                        operation.addParametersItem(new Parameter().in("header")
                            .name("encrypt-type")
                            .description("encrypt-type")
                            .required(false)
                            .schema(
                                new StringSchema()._default(
                                    "DISABLE")));
                    })
            );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat(JWT)
            .scheme("bearer");
    }

    @Component
    class CustomConverter extends ModelResolver {
        public CustomConverter(ObjectMapper mapper) {
            super(mapper, new TypeNameResolver() {
                @Override
                public String getNameOfClass(Class<?> cls) {
                    String fullName = cls.getName()
                        .replaceAll("\\$", "");
                    fullName = fullName.substring(fullName.lastIndexOf(".") + 1);
                    if (fullName.endsWith("Response")) {
                        fullName = fullName.replaceFirst("Response", "Result");
                    }
                    return fullName;
                }
            });
        }
    }
}
