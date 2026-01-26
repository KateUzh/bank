package ru.star.bank.rules;

import ru.star.bank.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;
/**
 * Интерфейс набора правил рекомендаций.
 *
 * <p>Каждая реализация интерфейса соответствует одному
 * банковскому продукту и содержит бизнес-логику проверки,
 * может ли данный продукт быть рекомендован пользователю.
 */
public interface RecommendationRuleSet {
    /**
     * Проверяет, подходит ли пользователь под правила рекомендации.
     *
     * @param userId уникальный идентификатор пользователя
     * @return Optional с рекомендацией, если правила выполнены,
     *         либо пустой Optional, если рекомендация не подходит
     */
    Optional<RecommendationDto> apply(UUID userId);
}
