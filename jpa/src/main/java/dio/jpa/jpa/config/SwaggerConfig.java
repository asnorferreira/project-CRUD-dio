package dio.jpa.jpa.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/users/**", "/login/**", "/**") // Incluindo todos os padrões de URL
                .addOpenApiCustomizer(openApiCustomiser())
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Exemplo de API REST com Spring Boot")
                        .description("API de exemplo utilizando Spring Boot, JPA e JWT para autenticação")
                        .version("1.0")
                        .termsOfService("http://www.seusite.com.br/termos-de-uso")
                        .license(new io.swagger.v3.oas.models.info.License().name("Licença - Sua Empresa")
                                .url("http://www.seusite.com.br"))
                        .contact(new Contact()
                                .name("Seu nome")
                                .url("http://www.seusite.com.br")
                                .email("voce@seusite.com.br")));
    }

    private OpenApiCustomizer openApiCustomiser() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                // Adiciona exemplos para operações de usuário
                if (operation.getTags().contains("User Operations")) {
                    operation.addParametersItem(new Parameter()
                            .name("id")
                            .description("ID do usuário")
                            .required(true));
                    operation.setResponses(new ApiResponses()
                            .addApiResponse("200", new ApiResponse().description("Operação bem-sucedida"))
                            .addApiResponse("404", new ApiResponse().description("Usuário não encontrado")));
                }
                // Adiciona exemplos para operações de login
                if (operation.getTags().contains("Login Operations")) {
                    operation.setRequestBody(
                            new io.swagger.v3.oas.models.parameters.RequestBody().description("Dados de login")
                                    .required(true));
                    operation.setResponses(new ApiResponses()
                            .addApiResponse("200", new ApiResponse().description("Login bem-sucedido"))
                            .addApiResponse("401", new ApiResponse().description("Credenciais inválidas")));
                }
            }));
        };
    }
}