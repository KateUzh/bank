package ru.star.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.service.DynamicRecommendationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DynamicRecommendationController {

    private final DynamicRecommendationService recommendationService;

    public DynamicRecommendationController(DynamicRecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public DynamicRecommendationDto createDynamicRecommendation(@RequestBody DynamicRecommendationDto recommendationDto){
        return recommendationService.addDynamicRecommendation(recommendationDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllDynamicRecommendations() {
        List<DynamicRecommendationDto> rules = recommendationService.getAllDynamicRecommendations();
        return ResponseEntity.ok().body(new Object() { public final List<DynamicRecommendationDto> data = rules; });
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> deleteDynamicRecommendation(@PathVariable("product_id") UUID productId) {
        recommendationService.deleteDynamicRecommendation(productId);
        return ResponseEntity.noContent().build();
    }
}
