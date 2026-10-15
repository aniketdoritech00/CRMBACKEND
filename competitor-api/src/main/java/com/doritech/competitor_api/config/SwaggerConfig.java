package com.doritech.competitor_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI competitorServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Competitor Service API")
                        .description("API for managing competitors in CRM system")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("CRM Team")
                                .email("crm-team@company.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
