package pro.sky.star_bank.recommendation.repository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.model.enums.EnumTransactionType;

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

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, false, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, false, true",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, true, false",
    })
    void checkRuleUserOf(UUID userId, EnumProductType productType, boolean negate, boolean expected) {
        boolean result = transactionsRepository.checkRuleUserOf(userId, productType, negate).orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, false, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, false, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, true, true",
    })
    void checkRuleActiveUserOf(UUID userId, EnumProductType productType, boolean negate, boolean expected) {
        boolean result = transactionsRepository.checkRuleActiveUserOf(userId, productType, negate).orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, DEPOSIT, >, 50000, false, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, DEPOSIT, >=, 50000, false, true",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, DEPOSIT, >=, 50000, true, false",
    })
    void checkRuleTransactionSumCompare(@NotNull UUID userId,
                                        @NotNull EnumProductType productType,
                                        @NotNull EnumTransactionType transactionType,
                                        @NotNull String comparator,
                                        @Min(0) int comparedValue,
                                        boolean negate,
                                        boolean expected) {
        boolean result = transactionsRepository
                .checkRuleTransactionSumCompare(userId, productType, transactionType, EnumCompareType.fromString(comparator), comparedValue, negate)
                .orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, <=, false, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, >=, false, true",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, >=, true, false",
    })
    void checkRuleTransactionSumCompareDepositWithdraw(@NotNull UUID userId,
                                                       @NotNull EnumProductType productType,
                                                       @NotNull String comparator,
                                                       boolean negate,
                                                       boolean expected) {
        boolean result = transactionsRepository
                .checkRuleTransactionSumCompareDepositWithdraw(userId, productType, EnumCompareType.fromString(comparator), negate)
                .orElse(false);

        assertThat(result).isEqualTo(expected);
    }
}