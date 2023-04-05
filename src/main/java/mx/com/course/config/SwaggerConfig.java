package mx.com.course.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI UdemyCartOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Udemy Microservice Cart Management Service")
            .description("This page lists all API's of Cart Management")
            .version("1.0.0")
            .contact(new Contact().name("Jesus Ponce").url("https://www.dev-team.com/").email("sponceisturiz@deloitte.com"))
            .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")))
            .externalDocs(new ExternalDocumentation()
            .description("Udemy Checkout Wiki Documentation")
            .url("http://springdoc.org"));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("udemy-cart")
                .pathsToMatch("/cart/**")
                .build();
    }
}
