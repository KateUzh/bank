package ru.star.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.star.bank.entity.DynamicRecommendationEntity;

public interface DynamicRecommendationRepository extends JpaRepository<DynamicRecommendationEntity, Long> {

}
