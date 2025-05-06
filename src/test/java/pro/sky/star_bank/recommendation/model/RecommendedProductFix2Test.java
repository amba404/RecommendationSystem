package pro.sky.star_bank.recommendation.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecommendedProductFix2Test {

    @Autowired
    RecommendedProductFix2 recommendedProduct;

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, false",
            "d4a4d619-9a0c-4fc5-b0cb-76c49409546b, true"
    })
    void checkForUser(UUID userId, boolean expected) {
        boolean result = recommendedProduct.checkForUser(userId);

        assertThat(result).isEqualTo(expected);
    }
}