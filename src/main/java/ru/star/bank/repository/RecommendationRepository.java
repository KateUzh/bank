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

//    public int getRandomTransactionAmount(UUID users){
//        Integer result = jdbcTemplate.queryForObject(
//                "SELECT amount FROM transactions t WHERE t.user_id = ? LIMIT 1",
//                Integer.class,
//                users);
//        return result != null ? result : 0;
//    }
}
