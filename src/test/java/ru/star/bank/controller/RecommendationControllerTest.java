package ru.star.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.service.RecommendationService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Юнит-тест для {@link RecommendationController}.
 *
 * <p>Использует {@link MockMvc} для тестирования REST-эндпоинта получения рекомендаций
 * по UUID пользователя без необходимости поднимать весь Spring контекст.
 *
 * <p>Тест проверяет следующие сценарии:
 * <ul>
 *     <li>Возврат корректного HTTP-статуса (200 OK).</li>
 *     <li>Правильное формирование JSON-ответа, включая userId и список рекомендаций.</li>
 *     <li>Обработка списка рекомендаций разного размера (в том числе большого списка из 1000 элементов).</li>
 * </ul>
 *
 * <p>Для изоляции контроллера используется анонимный {@link RecommendationService},
 * возвращающий заранее определенный {@link RecommendationResponse}.
 */
public class RecommendationControllerTest {

    private MockMvc mockMvc;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        RecommendationService service = new RecommendationService(null, null, null) {
            @Override
            public RecommendationResponse getRecommendations(UUID id) {
                return new RecommendationResponse(
                        id,
                        List.of(
                                new RecommendationDto(
                                        UUID.randomUUID(),
                                        "Test Recommendation 1",
                                        "Text 1"
                                ),
                                new RecommendationDto(
                                        UUID.randomUUID(),
                                        "Test Recommendation 2",
                                        "Text 2"
                                )
                        )
                );
            }
        };

        RecommendationController controller = new RecommendationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetRecommendations_StatusOkAndBody() throws Exception {
        mockMvc.perform(get("/recommendation/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(2))
                .andExpect(jsonPath("$.recommendations[0].productId").exists())
                .andExpect(jsonPath("$.recommendations[0].productName").value("Test Recommendation 1"))
                .andExpect(jsonPath("$.recommendations[0].productText").value("Text 1"));
    }

    @Test
    void testGetRecommendations_SecondElement() throws Exception {
        mockMvc.perform(get("/recommendation/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendations[1].productName").value("Test Recommendation 2"))
                .andExpect(jsonPath("$.recommendations[1].productText").value("Text 2"));
    }

    @Test
    void testGetRecommendations_LargeList() throws Exception {
        RecommendationService largeService = new RecommendationService(null, null, null) {
            @Override
            public RecommendationResponse getRecommendations(UUID id) {
                var list = new java.util.ArrayList<RecommendationDto>();
                for (int i = 0; i < 1000; i++) {
                    list.add(new RecommendationDto(
                            UUID.randomUUID(),
                            "Name" + i,
                            "Text" + i
                    ));
                }
                return new RecommendationResponse(id, list);
            }
        };

        RecommendationController controller = new RecommendationController(largeService);
        MockMvc localMockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        localMockMvc.perform(get("/recommendation/{user_id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendations.length()").value(1000));
    }
}
