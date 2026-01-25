package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.mapper.DynamicRecommendationMapper;
import ru.star.bank.repository.DynamicRecommendationRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * Юнит-тест для {@link DynamicRecommendationService}.
 *
 * <p>Проверяет корректность работы сервиса динамических рекомендаций, включая:
 * <ul>
 *     <li>Добавление новой рекомендации</li>
 *     <li>Получение всех рекомендаций</li>
 *     <li>Удаление рекомендации по productId</li>
 * </ul>
 *
 * <p>Используются моки для {@link DynamicRecommendationRepository}, а {@link DynamicRecommendationMapper}
 * используется реальный для проверки корректного преобразования DTO ↔ Entity.
 *
 * <p>Проверяется, что методы сервиса корректно взаимодействуют с репозиторием
 * и возвращают ожидаемые DTO.
 */
public class DynamicRecommendationServiceTest {

    private DynamicRecommendationRepository repository;
    private DynamicRecommendationMapper mapper;
    private DynamicRecommendationService service;

    @BeforeEach
    void setUp() {
        repository = mock(DynamicRecommendationRepository.class);
        mapper = new DynamicRecommendationMapper();
        service = new DynamicRecommendationService(repository, mapper);
    }

    @Test
    void testAddDynamicRecommendation() {
        DynamicRecommendationDto dto = new DynamicRecommendationDto("Prod", UUID.randomUUID(), "Text", null);
        DynamicRecommendationEntity entity = mapper.dynamicRecommendationDtoToEntity(dto);
        when(repository.save(any())).thenReturn(entity);

        DynamicRecommendationDto saved = service.addDynamicRecommendation(dto);
        assertEquals(dto.getProduct_name(), saved.getProduct_name());
    }

    @Test
    void testGetAllDynamicRecommendations() {
        DynamicRecommendationEntity entity1 = new DynamicRecommendationEntity("Prod1", UUID.randomUUID(), "Text1", null);
        DynamicRecommendationEntity entity2 = new DynamicRecommendationEntity("Prod2", UUID.randomUUID(), "Text2", null);
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<DynamicRecommendationDto> all = service.getAllDynamicRecommendations();
        assertEquals(2, all.size());
    }

    @Test
    void testDeleteDynamicRecommendation() {
        UUID id = UUID.randomUUID();
        DynamicRecommendationEntity entity = new DynamicRecommendationEntity("Prod", id, "Text", null);
        when(repository.findAll()).thenReturn(List.of(entity));

        service.deleteDynamicRecommendation(id);
        verify(repository, times(1)).delete(entity);
    }
}
