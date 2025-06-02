package pro.sky.star_bank.recommendation.repository;

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
import pro.sky.star_bank.recommendation.model.TransactionsUser;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.model.enums.EnumTransactionType;

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
     * Устарело
     * Возвращает список рекомендаций для пользователя
     *
     * @param userId идентификатор пользователя
     * @return список ID рекомендаций
     */
    @Deprecated
    public List<UUID> getRecommendations(UUID userId) {
        String sql = """
                WITH base AS (
                	SELECT
                		p.TYPE,
                		SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) AS DEPOSIT,
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END) AS WITHDRAW
                	FROM
                		transactions t
                	INNER JOIN PRODUCTS p ON
                		t.PRODUCT_ID = p.ID
                	WHERE
                		t.user_id = ?
                	GROUP BY
                		p.TYPE),
                	ruleDebitCntGt0         AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'DEBIT'),
                	ruleInvestCntEq0        AS (SELECT count(*) = 0 AS cnd FROM base WHERE TYPE = 'INVEST'),
                	ruleSavingDepGt1000     AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'SAVING' and deposit > 1000 ),
                	ruleDebitDepGe50000     AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'DEBIT' and deposit >= 50000),
                	ruleSavingDepGe50000    AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'SAVING' and deposit >= 50000),
                	ruleDebitDepGtWdrw      AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'DEBIT' and deposit > withdraw),
                	ruleDebitWdrwGt100000   AS (SELECT count(*) > 0 AS cnd FROM base WHERE TYPE = 'DEBIT' and withdraw > 100000),
                	ruleCreditCntEq0        AS (SELECT count(*) = 0 AS cnd FROM base WHERE TYPE = 'CREDIT'),
                	ruleAnd1 AS
                	(SELECT
                	    (SELECT cnd FROM ruleDebitCntGt0)
                		AND
                		(SELECT cnd FROM ruleInvestCntEq0)
                		AND
                		(SELECT cnd FROM ruleSavingDepGt1000)
                	    as cnd
                	from dual),
                	ruleOr2 AS
                	(SELECT
                        (SELECT cnd FROM ruleDebitDepGe50000)
                        OR
                        (SELECT cnd FROM ruleSavingDepGe50000)
                	    as cnd
                	from dual),
                	ruleAnd2 AS
                	(SELECT
                        (SELECT cnd FROM ruleDebitCntGt0)
                        AND
                        (SELECT cnd FROM ruleOr2)
                        AND
                        (SELECT cnd FROM ruleDebitDepGtWdrw)
                	    as cnd
                	from dual),
                	ruleAnd3 AS
                	(SELECT
                        (SELECT cnd FROM ruleCreditCntEq0)
                        AND
                        (SELECT cnd FROM ruleDebitDepGtWdrw)
                        AND
                        (SELECT cnd FROM ruleDebitWdrwGt100000)
                	    as cnd
                	from dual),
                	recom1 AS (
                	SELECT
                		'147f6a0f-3b91-413b-ab99-87f081d60d5a' AS id,
                		(SELECT cnd FROM ruleAnd1) AS is_ok
                	FROM dual),
                	recom2 AS (
                	SELECT
                		'59efc529-2fff-41af-baff-90ccd7402925' AS id,
                		(SELECT cnd FROM ruleAnd2)  AS is_ok
                	FROM dual
                	),
                	recom3 AS (
                	SELECT
                		'ab138afb-f3ba-4a93-b74f-0fcee86d447f' AS id,
                		(SELECT cnd FROM ruleAnd3)  AS is_ok
                	FROM dual
                	),
                    recom AS (
                    SELECT id, is_ok FROM recom1
                    UNION ALL
                    SELECT id, is_ok FROM recom2
                    UNION ALL
                    SELECT id, is_ok FROM recom3
                    )
                
                    SELECT  UUID() AS id FROM base WHERE FALSE
                    UNION ALL
                    SELECT id from recom where is_ok = TRUE
                """;

        List<UUID> uuids = jdbcTemplate.queryForList(sql, UUID.class, userId);
        return List.copyOf(uuids);
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
