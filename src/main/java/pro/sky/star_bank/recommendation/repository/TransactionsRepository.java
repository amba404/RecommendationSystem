package pro.sky.star_bank.recommendation.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("transactionsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(UUID user){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions t WHERE t.user_id = ? ORDER BY RAND() LIMIT 1",
                Integer.class,
                user);
        return result != null ? result : 0;
    }
}
