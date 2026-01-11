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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
