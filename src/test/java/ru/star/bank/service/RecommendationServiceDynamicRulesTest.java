package ru.star.bank.service;

import org.junit.jupiter.api.Test;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.QueryType;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Юнит-тест для {@link RecommendationService}, проверяющий обработку динамических правил.
 *
 * <p>Тестируется корректность применения {@link DynamicRuleEntity} для формирования
 * динамических рекомендаций, включая:
 * <ul>
 *     <li>Правило USER_OF, когда условие выполняется</li>
 *     <li>Правило USER_OF, когда условие не выполняется</li>
 *     <li>Правило USER_OF с инверсией (negate = true)</li>
 * </ul>
 *
 * <p>Используются моки для {@link RecommendationRepository} и {@link DynamicRecommendationRepository}
 * для имитации поведения базы данных и пользовательских продуктов.
 *
 * <p>Проверяется, что сервис корректно формирует список рекомендаций
 * в зависимости от результатов применения динамических правил и флага negate.
 */
public class RecommendationServiceDynamicRulesTest {

    @Test
    void dynamicRule_userOf_true() {
        UUID userId = UUID.randomUUID();

        RecommendationRepository userRepository = mock(RecommendationRepository.class);
        DynamicRecommendationRepository dynamicRepository = mock(DynamicRecommendationRepository.class);

        when(userRepository.hasProductOfType(userId, "DEBIT")).thenReturn(true);

        ArgumentsEntity arg = new ArgumentsEntity();
        arg.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(QueryType.USER_OF);
        rule.setArgumentsEntity(List.of(arg));
        rule.setNegate(false);

        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductId(UUID.randomUUID());
        entity.setProductName("Dynamic Name");
        entity.setProductText("Dynamic Text");
        entity.setRules(List.of(rule));

        when(dynamicRepository.findAll()).thenReturn(List.of(entity));

        RecommendationService service = new RecommendationService(
                List.of(),
                dynamicRepository,
                userRepository
        );

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("Dynamic Name", response.getRecommendations().get(0).getProductName());
    }

    @Test
    void dynamicRule_userOf_false() {
        UUID userId = UUID.randomUUID();

        RecommendationRepository userRepository = mock(RecommendationRepository.class);
        DynamicRecommendationRepository dynamicRepository = mock(DynamicRecommendationRepository.class);

        when(userRepository.hasProductOfType(userId, "DEBIT")).thenReturn(false);

        ArgumentsEntity arg = new ArgumentsEntity();
        arg.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(QueryType.USER_OF);
        rule.setArgumentsEntity(List.of(arg));
        rule.setNegate(false);

        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setRules(List.of(rule));

        when(dynamicRepository.findAll()).thenReturn(List.of(entity));

        RecommendationService service = new RecommendationService(
                List.of(),
                dynamicRepository,
                userRepository
        );

        RecommendationResponse response = service.getRecommendations(userId);

        assertTrue(response.getRecommendations().isEmpty());
    }

    @Test
    void dynamicRule_negate_true() {
        UUID userId = UUID.randomUUID();

        RecommendationRepository userRepository = mock(RecommendationRepository.class);
        DynamicRecommendationRepository dynamicRepository = mock(DynamicRecommendationRepository.class);

        when(userRepository.hasProductOfType(userId, "DEBIT")).thenReturn(false);

        ArgumentsEntity arg = new ArgumentsEntity();
        arg.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(QueryType.USER_OF);
        rule.setArgumentsEntity(List.of(arg));
        rule.setNegate(true);

        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductId(UUID.randomUUID());
        entity.setProductName("Negated");
        entity.setProductText("Works");
        entity.setRules(List.of(rule));

        when(dynamicRepository.findAll()).thenReturn(List.of(entity));

        RecommendationService service = new RecommendationService(
                List.of(),
                dynamicRepository,
                userRepository
        );

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("Negated", response.getRecommendations().get(0).getProductName());
    }
}
