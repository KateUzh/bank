package ru.star.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.service.RecommendationService;

import java.util.UUID;
/**
 * REST-контроллер сервиса рекомендаций.
 *
 * <p>Предоставляет API для получения списка рекомендованных
 * банковских продуктов для конкретного пользователя.</p>
 *
 * <p>Основной сценарий использования:
 * клиент отправляет HTTP GET-запрос с идентификатором пользователя,
 * сервис анализирует данные и возвращает список рекомендаций.</p>
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }
    /**
     * Возвращает список рекомендаций для пользователя.
     *
     * @param userId уникальный идентификатор пользователя банка
     * @return объект с идентификатором пользователя и списком рекомендаций;
     *         если рекомендаций нет, список будет пустым
     */
    @GetMapping("/{user_id}")
    public RecommendationResponse getRecommendations(@PathVariable("user_id") UUID userId) {
        return service.getRecommendations(userId);
    }
}
