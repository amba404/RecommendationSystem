package pro.sky.recommendation.system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class RecommendationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationSystemApplication.class, args);
    }

}
