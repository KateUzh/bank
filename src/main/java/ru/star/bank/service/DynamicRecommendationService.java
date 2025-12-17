package ru.star.bank.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.mapper.DynamicRecommendationMapper;
import ru.star.bank.repository.DynamicRecommendationRepository;

@Service
public class DynamicRecommendationService {

    private final DynamicRecommendationRepository recommendationRepository;
    private final DynamicRecommendationMapper recommendationMapper;

    public DynamicRecommendationService(DynamicRecommendationRepository recommendationRepository,
                                        DynamicRecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Transactional
    public DynamicRecommendationDto addDynamicRecommendation(DynamicRecommendationDto recommendationDto) {
        DynamicRecommendationEntity recommendationEntity =
                recommendationMapper.dynamicRecommendationDtoToEntity(recommendationDto);
        DynamicRecommendationEntity recommendationEntitySaved = recommendationRepository.save(recommendationEntity);
        return recommendationMapper.dynamicRecommendationEntityToDto(recommendationEntitySaved);
    }
}
