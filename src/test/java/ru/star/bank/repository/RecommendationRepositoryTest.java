package ru.star.bank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationRepositoryTest {

    private final UUID userId = UUID.randomUUID();
    private JdbcTemplate jdbcTemplate;
    private RecommendationRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new RecommendationRepository(jdbcTemplate);
    }

    @Test
    void testHasProductOfTypeTrue() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEBIT")))
                .thenReturn(3);

        boolean result = repository.hasProductOfType(userId, "DEBIT");
        assertTrue(result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEBIT"));
    }

    @Test
    void testHasProductOfTypeFalse() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CREDIT")))
                .thenReturn(0);

        boolean result = repository.hasProductOfType(userId, "CREDIT");
        assertFalse(result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CREDIT"));
    }

    @Test
    void testGetSumOfTransactions() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEBIT"), eq("DEPOSIT")))
                .thenReturn(5000);

        int sum = repository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT");
        assertEquals(5000, sum);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEBIT"), eq("DEPOSIT"));
    }

    @Test
    void testGetSumOfTransactionsReturnsZeroIfNull() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("SAVING"), eq("WITHDRAW")))
                .thenReturn(null);

        int sum = repository.getSumOfTransactions(userId, "SAVING", "WITHDRAW");
        assertEquals(0, sum);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("SAVING"), eq("WITHDRAW"));
    }

    @Test
    void testGetSumOfAllTransactions() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEPOSIT")))
                .thenReturn(10000);

        int sum = repository.getSumOfAllTransactions(userId, "DEPOSIT");
        assertEquals(10000, sum);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("DEPOSIT"));
    }

    @Test
    void testGetSumOfAllTransactionsReturnsZeroIfNull() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("WITHDRAW")))
                .thenReturn(null);

        int sum = repository.getSumOfAllTransactions(userId, "WITHDRAW");
        assertEquals(0, sum);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId), eq("WITHDRAW"));
    }
}
