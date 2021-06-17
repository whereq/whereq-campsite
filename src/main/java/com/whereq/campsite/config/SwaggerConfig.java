package com.whereq.campsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.whereq.campsite.web.rest"))
        .paths(regex("/api/.*"))
        .build()
        .apiInfo(metaData());
  }

  private ApiInfo metaData() {
    Contact contact = new Contact(
        "Tony Zhang",
        "https://www.whereq.com",
        "googol.zhang@gmail.com"
    );

    List<VendorExtension> vendorExtensionList = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
        "Spring Boot REST API",
        "Spring Boot REST API for WhereQ - Campsite",
        "1.0",
        "",
        contact,
        "Apache License Version 2.0",
        "https://www.apache.org/licenses/LICENSE-2.0",
        vendorExtensionList);
    return apiInfo;
  }
}
