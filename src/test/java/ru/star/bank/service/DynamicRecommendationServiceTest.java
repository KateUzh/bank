package ru.star.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.mapper.DynamicRecommendationMapper;
import ru.star.bank.repository.DynamicRecommendationRepository;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DynamicRecommendationServiceTest {

    @Mock
    private DynamicRecommendationRepository repository;

    @Mock
    private DynamicRecommendationMapper mapper;

    @InjectMocks
    private DynamicRecommendationService service;

    @Test
    void testAddDynamicRecommendation() {
        UUID productId = UUID.randomUUID();

        DynamicRecommendationDto dto = new DynamicRecommendationDto();
        dto.setProduct_id(productId);
        dto.setProduct_name("Test Product");
        dto.setProduct_text("Test Recommendation");
        dto.setRule(Collections.emptyList());

        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductId(productId);
        entity.setProductName("Test Product");
        entity.setProductText("Test Recommendation");
        entity.setRules(Collections.emptyList());

        DynamicRecommendationEntity savedEntity = new DynamicRecommendationEntity();
        savedEntity.setProductId(productId);
        savedEntity.setProductName("Test Product");
        savedEntity.setProductText("Test Recommendation");
        savedEntity.setRules(Collections.emptyList());
        savedEntity.setId(UUID.randomUUID());

        when(mapper.dynamicRecommendationDtoToEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.dynamicRecommendationEntityToDto(savedEntity)).thenReturn(dto);

        DynamicRecommendationDto result = service.addDynamicRecommendation(dto);

        assertEquals(dto.getProduct_id(), result.getProduct_id());
        assertEquals(dto.getProduct_name(), result.getProduct_name());
        assertEquals(dto.getProduct_text(), result.getProduct_text());
    }
}
