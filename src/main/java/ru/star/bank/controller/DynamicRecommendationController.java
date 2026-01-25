package ru.star.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.service.DynamicRecommendationService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * REST-контроллер для управления динамическими рекомендациями.
 *
 * <p>Обеспечивает CRUD-операции для динамических рекомендаций продуктов банка.
 * Используется в административной панели или внутреннем API.
 *
 * <p><b>Маршруты:</b>
 * <ul>
 *     <li><b>POST /rule</b> — создать новую динамическую рекомендацию. Принимает {@link DynamicRecommendationDto} в теле запроса и возвращает созданный объект.</li>
 *     <li><b>GET /rule</b> — получить список всех динамических рекомендаций. Возвращает JSON вида <code>{ "data": [...] }</code>.</li>
 *     <li><b>DELETE /rule/{product_id}</b> — удалить динамическую рекомендацию по идентификатору продукта (UUID). Возвращает HTTP 204 No Content при успешном удалении.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/rule")
public class DynamicRecommendationController {

    private final DynamicRecommendationService recommendationService;

    public DynamicRecommendationController(DynamicRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public DynamicRecommendationDto createDynamicRecommendation(@RequestBody DynamicRecommendationDto recommendationDto) {
        return recommendationService.addDynamicRecommendation(recommendationDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllDynamicRecommendations() {
        List<DynamicRecommendationDto> rules = recommendationService.getAllDynamicRecommendations();
        return ResponseEntity.ok().body(Map.of("data", rules));
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> deleteDynamicRecommendation(@PathVariable("product_id") UUID productId) {
        recommendationService.deleteDynamicRecommendation(productId);
        return ResponseEntity.noContent().build();
    }
}
