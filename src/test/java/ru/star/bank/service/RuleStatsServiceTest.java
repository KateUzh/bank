package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.entity.DynamicRuleEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Тестовый класс для проверки функциональности {@link RuleStatsService}.
 * <p>
 * В данном классе реализованы тесты для проверки корректности увеличения,
 * удаления и получения статистики по правилам.
 */
class RuleStatsServiceTest {

    private RuleStatsService service;

    @BeforeEach
    void setUp() {
        service = new RuleStatsService();
    }

    @Test
    void incrementHit_shouldIncreaseCounter() {
        Long ruleId = 1L;
        service.incrementHit(ruleId);
        service.incrementHit(ruleId);

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setId(ruleId); // важно!

        List<Map<String, Object>> stats = service.getAllStats(List.of(rule));
        assertEquals(1, stats.size(), "Должен возвращать корректное количество элементов");
        assertEquals(2, stats.get(0).get("count"));
    }


    @Test
    void removeHitCounter_shouldRemoveCounter() {
        Long ruleId = 2L;
        service.incrementHit(ruleId);
        service.removeHitCounter(ruleId);

        List<Map<String, Object>> stats = service.getAllStats(List.of());
        assertTrue(stats.isEmpty(), "Счетчик должен быть удален");
    }

    @Test
    void getAllStats_withRules_shouldReturnCorrectMap() {
        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setId(5L);

        service.incrementHit(5L);
        service.incrementHit(5L);

        List<Map<String, Object>> stats = service.getAllStats(List.of(rule));
        assertEquals(1, stats.size());
        Map<String, Object> stat = stats.get(0);
        assertEquals(5L, stat.get("rule_id"));
        assertEquals(2, stat.get("count"));
    }

    @Test
    void getAllStats_noRules_shouldReturnEmptyList() {
        List<Map<String, Object>> stats = service.getAllStats();
        assertNotNull(stats);
        assertTrue(stats.isEmpty(), "Список статистики должен быть пустым");
    }
}
