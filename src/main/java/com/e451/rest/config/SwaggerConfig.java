package com.e451.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static com.google.common.base.Predicates.not;

/**
 * Created by e384873 on 6/23/2017.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    @Profile("local")
    public Docket codeApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
                .apis(RequestHandlerSelectors.any())
                .paths(not(PathSelectors.regex("/error.*")))
                .build()
            .pathMapping("/api/v1")
            .genericModelSubstitutes(ResponseEntity.class)
            .useDefaultResponseMessages(false)
            .securitySchemes(Arrays.asList(apiKey()))
            .enableUrlTemplating(true)
            .apiInfo(metadata())
            .tags(new Tag("CoDE Service", "All apis relating to CoDE BFF"));
    }

    private ApiKey apiKey() {
        return new ApiKey("token", "Authorization", "header");
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("CoDE API")
                .description("The CoDE API")
                .version("1.0")
                .contact(new Contact("84.51", "http://8451.com", "Engineering-OwenTeam@8451.com"))
                .license("Proprietary")
                .build();
    }
}
