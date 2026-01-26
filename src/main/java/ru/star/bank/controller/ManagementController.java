package ru.star.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.service.RuleStatsService;

import java.util.List;
import java.util.Map;
/**
 * REST-контроллер для управления административными функциями и статистикой правил.
 *
 * <p>Предоставляет эндпоинты для:
 * <ul>
 *     <li>очистки кэшей JDBC-запросов;</li>
 *     <li>получения информации о приложении;</li>
 *     <li>просмотра статистики срабатываний правил рекомендаций.</li>
 * </ul>
 *
 * <p><b>Маршруты:</b>
 * <ul>
 *     <li><b>POST /management/clear-caches</b> — очищает все кэши JDBC (hasProductCache, sumCache, sumDepositWithdrawCache). Возвращает HTTP 204 No Content.</li>
 *     <li><b>GET /management/info</b> — возвращает базовую информацию о приложении: имя и версию в JSON формате <code>{ "name": "...", "version": "..." }</code>.</li>
 *     <li><b>GET /rule/stats</b> — возвращает статистику срабатываний правил в формате JSON: <code>{ "stats": [ { "rule_id": "...", "count": ... }, ... ] }</code>.</li>
 * </ul>
 */
@RestController
public class ManagementController {

    private final RecommendationRepository recommendationRepository;
    private final RuleStatsService ruleStatsService;

    public ManagementController(RecommendationRepository recommendationRepository,
                                RuleStatsService ruleStatsService) {
        this.recommendationRepository = recommendationRepository;
        this.ruleStatsService = ruleStatsService;
    }

    @PostMapping("/management/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        recommendationRepository.clearCaches();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/management/info")
    public Map<String, String> getInfo() {
        return Map.of(
                "name", "bank",
                "version", getClass().getPackage().getImplementationVersion() != null
                        ? getClass().getPackage().getImplementationVersion()
                        : "0.0.1-SNAPSHOT"
        );
    }

    @GetMapping("/rule/stats")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getRuleStats() {
        List<Map<String, Object>> stats = ruleStatsService.getAllStats();
        return ResponseEntity.ok(Map.of("stats", stats));
    }
}
