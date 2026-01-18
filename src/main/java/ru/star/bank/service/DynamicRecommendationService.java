package ru.star.bank.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.mapper.DynamicRecommendationMapper;
import ru.star.bank.repository.DynamicRecommendationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        DynamicRecommendationEntity entity = recommendationMapper.dynamicRecommendationDtoToEntity(recommendationDto);
        DynamicRecommendationEntity saved = recommendationRepository.save(entity);
        return recommendationMapper.dynamicRecommendationEntityToDto(saved);
    }

    public List<DynamicRecommendationDto> getAllDynamicRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(recommendationMapper::dynamicRecommendationEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDynamicRecommendation(UUID productId) {
        Optional<DynamicRecommendationEntity> entityOpt = recommendationRepository.findAll()
                .stream()
                .filter(e -> e.getProductId().equals(productId))
                .findFirst();
        entityOpt.ifPresent(recommendationRepository::delete);
    }
}
