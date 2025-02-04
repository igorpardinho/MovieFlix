package br.com.movieflix.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenAPI() {

        Contact contact = new Contact();
        contact.setUrl("https://github.com/igorpardinho/movieflix");

        Info info = new Info();
        info.setTitle("Movie flix");
        info.setDescription("API que gerencia catalogos de filmes");
        info.setVersion("1.0.0");
        info.setContact(contact);


        return new OpenAPI().info(info);
    }
}
