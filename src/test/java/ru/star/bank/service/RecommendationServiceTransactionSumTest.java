package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.QueryType;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Тестовый класс для проверки работы сервиса рекомендаций {@link RecommendationService}
 * с различными сценариями сравнения суммы транзакций пользователя.
 * <p>
 * В данном классе реализованы тесты для проверки условий: больше, меньше и равно суммы транзакции,
 * чтобы убедиться, что рекомендации формируются корректно в зависимости от условий.
 */
public class RecommendationServiceTransactionSumTest {

    private RecommendationRepository userRepository;
    private DynamicRecommendationRepository dynamicRepository;
    private RecommendationService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(RecommendationRepository.class);
        dynamicRepository = mock(DynamicRecommendationRepository.class);

        service = new RecommendationService(List.of(), dynamicRepository, userRepository);
    }

    private DynamicRecommendationEntity createRecommendation(UUID userId, String operator, int threshold, int sum) {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("CARD");

        ArgumentsEntity arg2 = new ArgumentsEntity();
        arg2.setTransactionType("DEPOSIT");

        ArgumentsEntity arg3 = new ArgumentsEntity();
        arg3.setMathSign(operator);

        ArgumentsEntity arg4 = new ArgumentsEntity();
        arg4.setThresholdSum(threshold);

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(QueryType.TRANSACTION_SUM_COMPARE);
        rule.setArgumentsEntity(List.of(arg1, arg2, arg3, arg4));
        rule.setNegate(false);

        DynamicRecommendationEntity recommendation = new DynamicRecommendationEntity();
        recommendation.setRules(List.of(rule));
        recommendation.setProductId(UUID.randomUUID());
        recommendation.setProductName("VIP CARD");
        recommendation.setProductText("Exclusive VIP CARD recommendation");

        when(userRepository.getSumOfTransactions(userId, "CARD", "DEPOSIT")).thenReturn(sum);

        return recommendation;
    }

    @Test
    void testTransactionSumGreater() {
        UUID userId = UUID.randomUUID();
        DynamicRecommendationEntity recommendation = createRecommendation(userId, ">", 5000, 6000);
        when(dynamicRepository.findAll()).thenReturn(List.of(recommendation));

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(1, response.getRecommendations().size());
        RecommendationDto dto = response.getRecommendations().get(0);
        assertEquals("VIP CARD", dto.getProductName());
    }

    @Test
    void testTransactionSumLess() {
        UUID userId = UUID.randomUUID();
        DynamicRecommendationEntity recommendation = createRecommendation(userId, "<", 5000, 4000);
        when(dynamicRepository.findAll()).thenReturn(List.of(recommendation));

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(1, response.getRecommendations().size());
        RecommendationDto dto = response.getRecommendations().get(0);
        assertEquals("VIP CARD", dto.getProductName());
    }

    @Test
    void testTransactionSumEquals() {
        UUID userId = UUID.randomUUID();
        DynamicRecommendationEntity recommendation = createRecommendation(userId, "=", 5000, 5000);
        when(dynamicRepository.findAll()).thenReturn(List.of(recommendation));

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(1, response.getRecommendations().size());
        RecommendationDto dto = response.getRecommendations().get(0);
        assertEquals("VIP CARD", dto.getProductName());
    }
}
