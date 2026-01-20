package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecommendationServiceDynamicTest {

    private RecommendationRepository userRepository;
    private DynamicRecommendationRepository dynamicRepository;
    private RecommendationService service;

    private RecommendationRuleSet ruleSet;

    @BeforeEach
    void setUp() {
        userRepository = mock(RecommendationRepository.class);
        dynamicRepository = mock(DynamicRecommendationRepository.class);

        ruleSet = userId -> Optional.of(new RecommendationDto(
                UUID.randomUUID(),
                "Test Recommendation",
                "Test Text"
        ));

        service = new RecommendationService(
                List.of(ruleSet),
                dynamicRepository,
                userRepository
        );
    }

    @Test
    void testFixedRuleRecommendation() {
        UUID userId = UUID.randomUUID();
        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(1, response.getRecommendations().size());

        RecommendationDto dto = response.getRecommendations().get(0);
        assertEquals("Test Recommendation", dto.getProductName());
        assertEquals("Test Text", dto.getProductText());
        assertNotNull(dto.getProductId());
    }

    @Test
    void testDynamicRuleRecommendation() {
        UUID userId = UUID.randomUUID();

        DynamicRecommendationEntity dynamicEntity = new DynamicRecommendationEntity();
        dynamicEntity.setProductId(UUID.randomUUID());
        dynamicEntity.setProductName("Dynamic Product");
        dynamicEntity.setProductText("Dynamic Text");
        dynamicEntity.setRules(List.of());

        when(dynamicRepository.findAll()).thenReturn(List.of(dynamicEntity));

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(2, response.getRecommendations().size());

        boolean foundDynamic = response.getRecommendations().stream()
                .anyMatch(dto -> dto.getProductName().equals("Dynamic Product") &&
                        dto.getProductText().equals("Dynamic Text"));
        assertTrue(foundDynamic);
    }

    @Test
    void testNoRecommendations() {
        UUID userId = UUID.randomUUID();

        when(dynamicRepository.findAll()).thenReturn(List.of());

        RecommendationRuleSet emptyRuleSet = u -> Optional.empty();
        service = new RecommendationService(
                List.of(emptyRuleSet),
                dynamicRepository,
                userRepository
        );

        RecommendationResponse response = service.getRecommendations(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertTrue(response.getRecommendations().isEmpty());
    }
}
