package ru.star.bank.service;

import org.springframework.stereotype.Service;
import ru.star.bank.entity.DynamicRuleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleStatsService {

    private final Map<Long, Integer> hitCounters = new HashMap<>();

    public void incrementHit(Long ruleId) {
        hitCounters.merge(ruleId, 1, Integer::sum);
    }

    public void removeHitCounter(Long ruleId) {
        hitCounters.remove(ruleId);
    }

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
