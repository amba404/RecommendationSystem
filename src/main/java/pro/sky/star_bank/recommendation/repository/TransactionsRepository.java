package pro.sky.star_bank.recommendation.repository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pro.sky.star_bank.recommendation.model.enums.EnumCompareType;
import pro.sky.star_bank.recommendation.model.enums.EnumProductType;
import pro.sky.star_bank.recommendation.model.enums.EnumTransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("transactionsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(UUID user) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? ORDER BY RAND() LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }

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

    public Optional<Boolean> checkTextRule(@NotEmpty String sql, UUID userId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
    }

    public Optional<Boolean> checkRuleUserOf(@NotNull UUID userId, @NotNull EnumProductType productType, boolean negate) {
        return checkRuleUserOf(userId, productType, negate, 1);
    }

    public Optional<Boolean> checkRuleActiveUserOf(@NotNull UUID userId, @NotNull EnumProductType productType, boolean negate) {
        return checkRuleUserOf(userId, productType, negate, 5);
    }

    public Optional<Boolean> checkRuleUserOf(@NotNull UUID userId, @NotNull EnumProductType productType, boolean negate, @Min(0) int count) {
        String sqlTemplate = """
                SELECT  %s (count(*) >= %d)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ?
                
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate.formatted(negate ? "NOT" : "", count)
                , Boolean.class
                , userId
                , productType.toString()));
    }

    public Optional<Boolean> checkRuleTransactionSumCompare(@NotNull UUID userId,
                                                            @NotNull EnumProductType productType,
                                                            @NotNull EnumTransactionType transactionType,
                                                            @NotNull EnumCompareType comparator,
                                                            @Min(0) int comparedValue,
                                                            boolean negate) {
        String sqlTemplate = """
                SELECT %s (sum(t.AMOUNT) %s %d)
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ? and t.TYPE = ?
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate.formatted(negate ? "NOT" : "", comparator, comparedValue)
                , Boolean.class
                , userId
                , productType.toString()
                , transactionType.toString()));
    }

    public Optional<Boolean> checkRuleTransactionSumCompareDepositWithdraw(@NotNull UUID userId,
                                                                           @NotNull EnumProductType productType,
                                                                           @NotNull EnumCompareType comparator,
                                                                           boolean negate) {
        String sqlTemplate = """
                SELECT %s (SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN amount ELSE 0 END) %s
                		SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN amount ELSE 0 END))
                FROM
                    transactions t
                        INNER JOIN PRODUCTS p ON
                        t.PRODUCT_ID = p.ID
                WHERE
                    t.user_id = ? and p.TYPE = ?
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sqlTemplate.formatted(negate ? "NOT" : "", comparator)
                , Boolean.class
                , userId
                , productType.toString()));
    }
}
