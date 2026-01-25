package ru.star.bank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationRepositoryTest {

    @Mock
    @Qualifier("recommendationsJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    RecommendationRepository repository;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }

    @Test
    void testHasProductOfType_true() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD")))
                .thenReturn(1);

        boolean result = repository.hasProductOfType(userId, "CARD");
        assertTrue(result);

        boolean cached = repository.hasProductOfType(userId, "CARD");
        assertTrue(cached);

        verify(jdbcTemplate, times(1))
                .queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD"));
    }

    @Test
    void testHasProductOfType_false() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD")))
                .thenReturn(0);

        boolean result = repository.hasProductOfType(userId, "CARD");
        assertFalse(result);
    }

    @Test
    void testGetSumOfTransactions() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD"), eq("DEPOSIT")))
                .thenReturn(500);

        int sum = repository.getSumOfTransactions(userId, "CARD", "DEPOSIT");
        assertEquals(500, sum);

        int cached = repository.getSumOfTransactions(userId, "CARD", "DEPOSIT");
        assertEquals(500, cached);
    }

    @Test
    void testGetSumOfAllTransactions() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("WITHDRAW")))
                .thenReturn(300);

        int sum = repository.getSumOfAllTransactions(userId, "WITHDRAW");
        assertEquals(300, sum);
    }

    @Test
    void testGetSumDepositWithdraw() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD"), eq("DEPOSIT")))
                .thenReturn(1000);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD"), eq("WITHDRAW")))
                .thenReturn(400);

        int result = repository.getSumDepositWithdraw(userId, "CARD");
        assertEquals(600, result);
    }

    @Test
    void testGetTransactionCount() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq("CARD")))
                .thenReturn(7);

        int count = repository.getTransactionCount(userId, "CARD");
        assertEquals(7, count);
    }
}
