package br.com.softdesign.career.votingservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class Swagger2Configuration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("softdesign")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.softdesign.career.votingservice"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Voting Service API")
                .version("0.0.1")
                .contact(contact())
                .build();
    }

    private Contact contact() {
        return new Contact("Rafael Andrade de Oliveira", "https://github.com/eurafa", "eu.rafa@gmail.com");
    }

}
