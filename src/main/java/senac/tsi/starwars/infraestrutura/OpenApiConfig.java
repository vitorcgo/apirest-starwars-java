package senac.tsi.starwars.infraestrutura;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Star Wars",
                version = "1.0.0",
                description = "API REST do universo Star Wars com Planetas, Espécies, Personagens, Naves e Filmes.",
                contact = @Contact(
                        name = "Turma TSI - Senac",
                        email = "tsi@senac.br"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org"
                )
        )
)
public class OpenApiConfig {
}
