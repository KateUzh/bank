package ru.star.bank.service;

import org.junit.jupiter.api.Test;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Юнит-тест для {@link RecommendationService}, проверяющий рекомендации,
 * формируемые только фиксированными правилами {@link RecommendationRuleSet}.
 *
 * <p>Сценарий теста:
 * <ul>
 *     <li>Создается фиксированное правило, которое всегда возвращает одну рекомендацию.</li>
 *     <li>Динамические рекомендации отсутствуют (пустой список в {@link DynamicRecommendationRepository}).</li>
 *     <li>Проверяется, что {@link RecommendationResponse} содержит корректную рекомендацию,
 *     соответствующую фиксированному правилу.</li>
 * </ul>
 *
 * <p>Моки {@link RecommendationRepository} и {@link DynamicRecommendationRepository} используются
 * для имитации базы данных и динамических правил.
 */
public class RecommendationServiceFixTest {

    @Test
    void fixedRecommendation_only() {
        UUID userId = UUID.randomUUID();

        RecommendationRuleSet ruleSet = id ->
                Optional.of(new RecommendationDto(
                        UUID.randomUUID(),
                        "Fixed Name",
                        "Fixed Text"
                ));

        RecommendationRepository userRepository = mock(RecommendationRepository.class);
        DynamicRecommendationRepository dynamicRepository = mock(DynamicRecommendationRepository.class);

        when(dynamicRepository.findAll()).thenReturn(List.of());

        RecommendationService service = new RecommendationService(
                List.of(ruleSet),
                dynamicRepository,
                userRepository
        );

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(1, response.getRecommendations().size());

        RecommendationDto dto = response.getRecommendations().get(0);
        assertEquals("Fixed Name", dto.getProductName());
        assertEquals("Fixed Text", dto.getProductText());
        assertNotNull(dto.getProductId());
    }
}
