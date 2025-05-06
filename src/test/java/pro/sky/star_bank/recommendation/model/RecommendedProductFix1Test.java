package pro.sky.star_bank.recommendation.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecommendedProductFix1Test {

    @Autowired
    RecommendedProductFix1 recommendedProduct;

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, true"
    })
    void checkForUser(UUID userId, boolean expected) {
        boolean result = recommendedProduct.checkForUser(userId);

        assertThat(result).isEqualTo(expected);
    }
}