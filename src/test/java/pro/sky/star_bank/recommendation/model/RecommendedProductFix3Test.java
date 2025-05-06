package pro.sky.star_bank.recommendation.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecommendedProductFix3Test {

    @Autowired
    RecommendedProductFix3 recommendedProduct;

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, false",
            "1f9b149c-6577-448a-bc94-16bea229b71a, true"
    })
    void checkForUser(UUID userId, boolean expected) {
        boolean result = recommendedProduct.checkForUser(userId);

        assertThat(result).isEqualTo(expected);
    }
}