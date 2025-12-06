package ru.star.bank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasProductOfType(UUID userId, String type) {
        String sql = """
                    SELECT COUNT(*) 
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, type);
        return count != null && count > 0;
    }

    public int getSumOfTransactions(UUID userId, String productType, String transactionType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount),0) 
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = ?
                """;
        Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, transactionType);
        return sum != null ? sum : 0;
    }

    public int getSumOfAllTransactions(UUID userId, String transactionType) {
        String sql = """
                    SELECT COALESCE(SUM(t.amount),0)
                    FROM transactions t
                    WHERE t.user_id = ? AND t.type = ?
                """;
        Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, transactionType);
        return sum != null ? sum : 0;
    }
}
