package com.robe_ortiz_questions.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.robe_ortiz_questions.entity.question.CategoryOfQuestion;
import com.robe_ortiz_questions.entity.question.TypeOfQuestion;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentación API Questions")
                        .version("1.0")
                        .description("Descripción de la API ofrecida para poder interactuar con el backend.<br>" +
                                     "Categorías válidas: " + 
                                     String.join(", ", Arrays.stream(CategoryOfQuestion.values())
                                             .map(Enum::name)
                                             .collect(Collectors.toList())) + "<br>" +
                                     "Tipos de pregunta válidos: " + 
                                     String.join(", ", Arrays.stream(TypeOfQuestion.values())
                                             .map(Enum::name)
                                             .collect(Collectors.toList())))
                );
    }


}
