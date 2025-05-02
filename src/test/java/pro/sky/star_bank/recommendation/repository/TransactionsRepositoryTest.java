package pro.sky.star_bank.recommendation.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionsRepositoryTest {

    @Autowired
    TransactionsRepository transactionsRepository;

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, 0",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, 2"
    })
    void getRecommendations(UUID userId, int sizeExpected) {
        List<UUID> recommendations2 = transactionsRepository.getRecommendations(userId);

        assertThat(recommendations2.size()).isEqualTo(sizeExpected);
    }
}