package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.RecommendationRuleSet;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecommendationServiceTest {

    private RecommendationRuleSet ruleSet1;
    private RecommendationRuleSet ruleSet2;
    private DynamicRecommendationRepository dynamicRepository;
    private RecommendationRepository userRepository;
    private RecommendationService service;
    private UUID userId;

    @BeforeEach
    void setup() {
        ruleSet1 = mock(RecommendationRuleSet.class);
        ruleSet2 = mock(RecommendationRuleSet.class);
        dynamicRepository = mock(DynamicRecommendationRepository.class);
        userRepository = mock(RecommendationRepository.class);
        userId = UUID.randomUUID();

        service = new RecommendationService(
                Arrays.asList(ruleSet1, ruleSet2),
                dynamicRepository,
                userRepository
        );
    }

    @Test
    void testFixedRecommendations() {
        RecommendationDto r1 = new RecommendationDto(UUID.randomUUID(), "Product1", "Text1");
        when(ruleSet1.apply(userId)).thenReturn(Optional.of(r1));
        when(ruleSet2.apply(userId)).thenReturn(Optional.empty());
        when(dynamicRepository.findAll()).thenReturn(Arrays.asList());

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(userId, response.getUserId());
        assertEquals(1, response.getRecommendations().size());
        assertTrue(response.getRecommendations().contains(r1));
    }

    @Test
    void testEmptyRecommendations() {
        when(ruleSet1.apply(userId)).thenReturn(Optional.empty());
        when(ruleSet2.apply(userId)).thenReturn(Optional.empty());
        when(dynamicRepository.findAll()).thenReturn(Arrays.asList());

        RecommendationResponse response = service.getRecommendations(userId);

        assertTrue(response.getRecommendations().isEmpty());
    }
}
