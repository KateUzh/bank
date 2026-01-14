package ru.star.bank.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> hasProductCache;
    private final Cache<String, Integer> sumCache;
    private final Cache<String, Integer> sumDepositWithdrawCache;

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        hasProductCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        sumCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        sumDepositWithdrawCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    public boolean hasProductOfType(UUID userId, String type) {
        String key = userId + "-" + type;
        return hasProductCache.get(key, k -> {
            String sql = """
                    SELECT COUNT(*) 
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                    """;
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, type);
            return count != null && count > 0;
        });
    }

    public int getSumOfTransactions(UUID userId, String productType, String transactionType) {
        String key = userId + "-" + productType + "-" + transactionType;
        return sumCache.get(key, k -> {
            String sql = """
                    SELECT COALESCE(SUM(t.amount),0) 
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = ?
                    """;
            Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, transactionType);
            return sum != null ? sum : 0;
        });
    }

    public int getSumOfAllTransactions(UUID userId, String transactionType) {
        String key = userId + "-" + transactionType;
        return sumCache.get(key, k -> {
            String sql = """
                    SELECT COALESCE(SUM(t.amount),0)
                    FROM transactions t
                    WHERE t.user_id = ? AND t.type = ?
                    """;
            Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, transactionType);
            return sum != null ? sum : 0;
        });
    }

    public int getSumDepositWithdraw(UUID userId, String productType) {
        String key = userId + "-" + productType;
        return sumDepositWithdrawCache.get(key, k -> {
            int deposit = getSumOfTransactions(userId, productType, "DEPOSIT");
            int withdraw = getSumOfTransactions(userId, productType, "WITHDRAW");
            return deposit - withdraw;
        });
    }

    public int getTransactionCount(UUID userId, String productType) {
        String sql = """
                    SELECT COUNT(*) 
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        return count != null ? count : 0;
    }
    public String getName(String username){
        String sql = """
                SELECT first_name
                FROM users u
                WHERE u.username = ?
                """;
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public String getSurname(String username){
        String sql = """
                SELECT last_name
                FROM users u
                WHERE u.username = ?
                """;
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public UUID getId(String username){
        String sql = """
                SELECT id
                FROM users u
                WHERE u.username = ?
                """;
        return jdbcTemplate.queryForObject(sql, UUID.class, username);
    }
}
