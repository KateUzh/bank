package ru.star.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.service.RuleStatsService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * Юнит-тест для {@link ManagementController}.
 *
 * <p>Тест проверяет корректность работы административных эндпоинтов:
 * <ul>
 *     <li>очистка кэшей JDBC-запросов;</li>
 *     <li>получение информации о приложении;</li>
 *     <li>получение статистики срабатываний правил.</li>
 * </ul>
 * </p>
 *
 * <p>Используются мок-объекты для {@link RecommendationRepository} и {@link RuleStatsService},
 * чтобы изолировать тестируемый контроллер от внешних зависимостей.</p>
 *
 * <p><b>Проверяемое поведение:</b>
 * <ul>
 *     <li>{@link ManagementController#clearCaches()} вызывает метод {@link RecommendationRepository#clearCaches()} и возвращает HTTP 204;</li>
 *     <li>{@link ManagementController#getInfo()} возвращает правильное имя приложения и версию;</li>
 *     <li>{@link ManagementController#getRuleStats()} вызывает {@link RuleStatsService#getAllStats()} и корректно формирует JSON с ключом "stats".</li>
 * </ul>
 * </p>
 */
class ManagementControllerTest {

    private RecommendationRepository recommendationRepository;
    private RuleStatsService ruleStatsService;
    private ManagementController controller;

    @BeforeEach
    void setUp() {
        recommendationRepository = mock(RecommendationRepository.class);
        ruleStatsService = mock(RuleStatsService.class);
        controller = new ManagementController(recommendationRepository, ruleStatsService);
    }

    @Test
    void testClearCaches() {
        ResponseEntity<Void> response = controller.clearCaches();
        verify(recommendationRepository, times(1)).clearCaches();
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetInfo() {
        Map<String, String> info = controller.getInfo();
        assertEquals("bank", info.get("name"));
        assertNotNull(info.get("version"));
    }

    @Test
    void testGetRuleStats() {
        when(ruleStatsService.getAllStats()).thenReturn(List.of(Map.of("rule_id", 1L, "count", 5)));

        ResponseEntity<Map<String, List<Map<String, Object>>>> response = controller.getRuleStats();
        verify(ruleStatsService, times(1)).getAllStats();

        Map<String, List<Map<String, Object>>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("stats"));
        assertEquals(1, body.get("stats").size());
        assertEquals(1L, body.get("stats").get(0).get("rule_id"));
        assertEquals(5, body.get("stats").get(0).get("count"));
    }
}
