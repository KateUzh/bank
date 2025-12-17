package ru.star.bank.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.service.DynamicRecommendationService;

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

//
//    @GetMapping
//    public List<DynamicRecommendation> getAllRecommendations (){
//        return dynamicRecommendationService.getAllRecommendations;
//    }
//
//    @DeleteMapping ("/{product_id}")
//    public deleteDynamicRecommendation () {
//        dynamicRecommendationService.deleteDynamicRecommendation;
//    }
}
