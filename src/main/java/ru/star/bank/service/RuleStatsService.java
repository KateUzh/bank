package ru.star.bank.service;

import org.springframework.stereotype.Service;
import ru.star.bank.entity.DynamicRuleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Сервис сбора статистики применения правил рекомендаций.
 *
 * <p>Хранит и предоставляет данные о количестве
 * срабатываний каждого правила.
 */
@Service
public class RuleStatsService {
    /**
     * Хранилище счётчиков срабатываний правил.
     * Ключ — ID правила, значение — количество срабатываний.
     */
    private final Map<Long, Integer> hitCounters = new HashMap<>();

    /**
     * Увеличивает счётчик срабатываний для указанного правила.
     *
     * <p>Если счётчик для данного правила отсутствует,
     * он будет создан и инициализирован значением {@code 1}.
     *
     * @param ruleId идентификатор правила
     */
    public void incrementHit(Long ruleId) {
        hitCounters.merge(ruleId, 1, Integer::sum);
    }

    /**
     * Удаляет статистику срабатываний для указанного правила.
     *
     * <p>Используется, например, при удалении правила
     * из системы.
     *
     * @param ruleId идентификатор правила
     */
    public void removeHitCounter(Long ruleId) {
        hitCounters.remove(ruleId);
    }
    /**
     * Возвращает статистику срабатываний для переданного списка правил.
     *
     * <p>Для каждого правила формируется запись со следующими полями:
     * <ul>
     *     <li>{@code rule_id} — идентификатор правила</li>
     *     <li>{@code count} — количество срабатываний</li>
     * </ul>
     * Если правило ещё не срабатывало, значение {@code count} будет равно {@code 0}.</p>
     *
     * @param rules список динамических правил
     * @return список карт со статистикой по каждому правилу
     */
    public List<Map<String, Object>> getAllStats(List<DynamicRuleEntity> rules) {
        return rules.stream()
                .map(rule -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("rule_id", rule.getId());
                    m.put("count", hitCounters.getOrDefault(rule.getId(), 0));
                    return m;
                })
                .toList();
    }

    public List<Map<String, Object>> getAllStats() {
        return getAllStats(List.of());
    }
}
