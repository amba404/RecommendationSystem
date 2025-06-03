package pro.sky.star.bank.recommendation.repository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pro.sky.star.bank.recommendation.model.TransactionsUser;
import pro.sky.star.bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star.bank.recommendation.model.enums.EnumProductType;
import pro.sky.star.bank.recommendation.model.enums.EnumTransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для получения данных по транзакциям: проверка выполнения правил рекомендации
 */
@Repository
public class TransactionsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger(TransactionsRepository.class);

    public TransactionsRepository(@Qualifier("transactionsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Возвращает случайную транзакцию пользователя
     *
     * @param user идентификатор пользователя
     * @return Сумма операции по случайной транзакции пользователя
     */
    public int getRandomTransactionAmount(UUID user) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? ORDER BY RAND() LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }

    /**
     * Проверяет выполнение правила, заданного текстом SQL
     *
     * @param userId идентификатор пользователя
     * @return True | false, или Null
     */
    private Optional<Boolean> checkTextRule(@NotEmpty String sql, UUID userId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
    }

    /**
     * Проверяет выполнение правил рекомендации фиксированного продукта Invest500
     *
     * @param userId идентификатор пользователя
     * @return True | false, или Null
     */
    public boolean checkForUserInvest500(UUID userId) {
        String sql1 = """
                SELECT count(*) > 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        String sql2 = """
                SELECT count(*) = 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'INVEST'
                """;

        String sql3 = """
                SELECT sum(t.AMOUNT) > 1000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'SAVING' and t.TYPE = 'DEPOSIT'
                """;

        return checkTextRule(sql1, userId).orElse(false)
                &&
                checkTextRule(sql2, userId).orElse(false)
                &&
                checkTextRule(sql3, userId).orElse(false);
    }

    /**
     * Проверяет выполнение правил рекомендации фиксированного продукта TopSaving
     *
     * @param userId идентификатор пользователя
     * @return True | false, или Null
     */
    public boolean checkForUserTopSaving(UUID userId) {
        String sql1 = """
                SELECT count(*) > 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        String sql2 = """
                SELECT sum(t.AMOUNT) >= 50000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT' and t.TYPE = 'DEPOSIT'
                """;

        String sql3 = """
                SELECT sum(t.AMOUNT) >= 50000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'SAVING' and t.TYPE = 'DEPOSIT'
                """;


        String sql4 = """
                SELECT SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) >
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        return checkTextRule(sql1, userId).orElse(false)
                &&
                (
                        checkTextRule(sql2, userId).orElse(false)
                                ||
                                checkTextRule(sql3, userId).orElse(false)
                )
                &&
                checkTextRule(sql4, userId).orElse(false);
    }

    /**
     * Проверяет выполнение правил рекомендации фиксированного продукта SimpleCredit
     *
     * @param userId идентификатор пользователя
     * @return True | false, или Null
     */
    public boolean checkForUserSimpleCredit(UUID userId) {
        String sql1 = """
                SELECT count(*) = 0
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'CREDIT'
                """;

        String sql2 = """
                SELECT SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) >
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT'
                """;

        String sql3 = """
                SELECT sum(t.AMOUNT) > 100000
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = 'DEBIT' and t.TYPE = 'WITHDRAW'
                """;

        return checkTextRule(sql1, userId).orElse(false)
                &&
                checkTextRule(sql2, userId).orElse(false)
                &&
                checkTextRule(sql3, userId).orElse(false);
    }

    /**
     * Проверяет выполнение динамического правила "Используется продукт с типом PRODUCT_TYPE заданное количество раз"
     *
     * @param userId      id пользователя
     * @param productType тип продукта
     * @param count       количество раз
     * @return True | false, или Null
     */
    @Cacheable(value = "UserOf", key = "{#userId, #productType, #count}")
    public Optional<Boolean> checkRuleUserOf(@NotNull UUID userId, @NotNull EnumProductType productType, @Min(0) int count) {
        logger.info("checkRuleUserOf");

        String sqlTemplate = """
                SELECT  count(*) >= ? as cnd
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ?
                
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate
                , Boolean.class
                , count
                , userId
                , productType.toString()));
    }

    /**
     * Проверяет выполнение динамического правила "Сумма операций по продукту PRODUCT_TYPE > VALUE"
     *
     * @param userId          id пользователя
     * @param productType     тип продукта
     * @param transactionType тип операции
     * @param comparator      операция сравнения
     * @param comparedValue   сравниваемое значение
     * @return True | false, или Null
     */
    @Cacheable(value = "TransactionSumCompare", key = "{#userId, #productType, #transactionType, #comparator, #comparedValue}")
    public Optional<Boolean> checkRuleTransactionSumCompare(@NotNull UUID userId,
                                                            @NotNull EnumProductType productType,
                                                            @NotNull EnumTransactionType transactionType,
                                                            @NotNull EnumCompareType comparator,
                                                            @Min(0) int comparedValue) {
        logger.info("checkRuleTransactionSumCompare");

        String sqlTemplate = """
                SELECT (sum(t.AMOUNT) %s %d)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ? and t.TYPE = ?
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate.formatted(comparator, comparedValue)
                , Boolean.class
                , userId
                , productType.toString()
                , transactionType.toString()));
    }

    /**
     * Проверяет выполнение динамического правила "Сумма пополнения и снятия средств по продукту PRODUCT_TYPE сравнивается заданным способом"
     *
     * @param userId      id пользователя
     * @param productType тип продукта
     * @param comparator  операция сравнения
     * @return True | false, или Null
     */
    @Cacheable(value = "TransactionSumCompareDepositWithdraw", key = "{#userId, #productType, #comparator}")
    public Optional<Boolean> checkRuleTransactionSumCompareDepositWithdraw(@NotNull UUID userId,
                                                                           @NotNull EnumProductType productType,
                                                                           @NotNull EnumCompareType comparator) {
        logger.info("checkRuleTransactionSumCompareDepositWithdraw");

        String sqlTemplate = """
                SELECT (SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) %s
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END))
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ?
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate.formatted(comparator)
                , Boolean.class
                , userId
                , productType.toString()));
    }

    public Optional<TransactionsUser> getUserByUserName(@NotNull String userName) {
        RowMapper<TransactionsUser> TransactionUserRowMapper = (rs, rowNum) ->
                new TransactionsUser(UUID.fromString(rs.getString("ID")),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("USERNAME"));

        TransactionsUser user = null;
        List<TransactionsUser> userList = jdbcTemplate.query("select * from users where username = ? limit 2",
                TransactionUserRowMapper,
                userName);

        if (userList.size() == 1) {
            user = userList.get(0);
        }
        return Optional.ofNullable(user);
    }
}
