package pro.sky.star.bank.recommendation.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.star.bank.recommendation.model.RecommendedProduct;
import pro.sky.star.bank.recommendation.service.implementations.RecommendationServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecommendationServiceImplTests {

    @Autowired
    RecommendationServiceImpl recommendationService;

    @ParameterizedTest
    @CsvSource({
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, 147f6a0f-3b91-413b-ab99-87f081d60d5a", // user 1
            "d4a4d619-9a0c-4fc5-b0cb-76c49409546b, 59efc529-2fff-41af-baff-90ccd7402925", // user 2
            "1f9b149c-6577-448a-bc94-16bea229b71a, ab138afb-f3ba-4a93-b74f-0fcee86d447f"  // user 3
    })
    public void testRecommendations(UUID userId, UUID productId) {
        List<RecommendedProduct> recommendations = recommendationService.getRecommendations(userId);

        long count = recommendations.stream()
                .filter(r -> r.getId().equals(productId))
                .count();

        assertThat(count).isEqualTo(1L);

    }
}
