package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {

    private RecommendationRepository userRepository;
    private DynamicRecommendationRepository dynamicRepository;
    private RecommendationService service;

    @BeforeEach
    void setUp() {
        userRepository = mock(RecommendationRepository.class);
        dynamicRepository = mock(DynamicRecommendationRepository.class);

        service = new RecommendationService(List.of(), dynamicRepository, userRepository);
    }

    @Test
    void testDynamicRecommendationWithRuleEvaluated() {
        UUID userId = UUID.randomUUID();

        when(userRepository.hasProductOfType(userId, "DEBIT")).thenReturn(true);

        ArgumentsEntity arg = new ArgumentsEntity();
        arg.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(QueryType.USER_OF);
        rule.setNegate(false);
        rule.setArgumentsEntity(List.of(arg));

        DynamicRecommendationEntity recommendation = new DynamicRecommendationEntity();
        recommendation.setProductId(UUID.randomUUID());
        recommendation.setProductName("Debit Card Promo");
        recommendation.setProductText("Рекомендуем открыть дебетовую карту");
        recommendation.setRules(List.of(rule));

        when(dynamicRepository.findAll()).thenReturn(List.of(recommendation));

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());

        verify(userRepository).hasProductOfType(userId, "DEBIT");
        verify(dynamicRepository).findAll();
    }
}
