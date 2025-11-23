package ru.star.bank.rules;

import ru.star.bank.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> apply(UUID userId);
}
