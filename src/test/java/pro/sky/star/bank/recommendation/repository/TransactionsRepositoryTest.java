package pro.sky.star.bank.recommendation.repository;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.star.bank.recommendation.model.TransactionsUser;
import pro.sky.star.bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star.bank.recommendation.model.enums.EnumProductType;
import pro.sky.star.bank.recommendation.model.enums.EnumTransactionType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionsRepositoryTest {

    @Resource
    TransactionsRepository transactionsRepository;

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, 1, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, 1, true",
    })
    void checkRuleUserOf(UUID userId, EnumProductType productType, int count, boolean expected) {
        boolean result = transactionsRepository.checkRuleUserOf(userId, productType, count).orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, DEPOSIT, >, 50000, false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, DEPOSIT, >=, 50000, true",
    })
    void checkRuleTransactionSumCompare(@NotNull UUID userId,
                                        @NotNull EnumProductType productType,
                                        @NotNull EnumTransactionType transactionType,
                                        @NotNull String comparator,
                                        @Min(0) int comparedValue,
                                        boolean expected) {
        boolean result = transactionsRepository
                .checkRuleTransactionSumCompare(userId, productType, transactionType, EnumCompareType.fromString(comparator), comparedValue)
                .orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, DEBIT, <=,  false",
            "cd515076-5d8a-44be-930e-8d4fcb79f42d, DEBIT, >=,  true",
    })
    void checkRuleTransactionSumCompareDepositWithdraw(@NotNull UUID userId,
                                                       @NotNull EnumProductType productType,
                                                       @NotNull String comparator,
                                                       boolean expected) {
        boolean result = transactionsRepository
                .checkRuleTransactionSumCompareDepositWithdraw(userId, productType, EnumCompareType.fromString(comparator))
                .orElse(false);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void CacheEnabledTest() {
        TransactionsRepository repo = transactionsRepository; //Mockito.mock(TransactionsRepository.class);
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        EnumProductType productType = EnumProductType.DEBIT;
        int cnt = 1;

        repo.checkRuleUserOf(userId, productType, cnt);
        repo.checkRuleUserOf(userId, productType, cnt);
        repo.checkRuleUserOf(userId, productType, cnt);

//        verify(repo, times(1)).checkRuleUserOf(userId, productType, negate, cnt);
    }

    @Test
    void getUserByName_NotFound() {
        TransactionsRepository repo = transactionsRepository;

        TransactionsUser result = repo.getUserByUserName("!unknown!").orElse(null);

        assertThat(result).isNull();
    }

    /**
     * Данный тест выполняется, если в таблице transactions.users
     * дублировать запись c username = "jordon.bergnaum".
     * Проверено. Тест Ок
     */
    @Disabled
    @Test
    void getUserByName_MoreThanOne() {
        TransactionsRepository repo = transactionsRepository;

        TransactionsUser result = repo.getUserByUserName("jordon.bergnaum").orElse(null);

        assertThat(result).isNull();
    }
}