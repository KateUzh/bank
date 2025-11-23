package ru.star.bank.service;

import org.springframework.stereotype.Service;
import ru.star.bank.repository.RecommendationRepository;

import java.util.UUID;

@Service
public class RecommendationService {
private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }
}

