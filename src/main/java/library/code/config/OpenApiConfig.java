package library.code.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI getCustomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electronic Library API")
                        .description("API для управления библиотечным фондом")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Карпова Дарья")
                                .email("d.karpova31@gmail.com")
                                .url("https://github.com/DariaKarpova3108")));
    }
}
