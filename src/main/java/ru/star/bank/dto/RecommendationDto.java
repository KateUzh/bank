package ru.star.bank.dto;

import java.util.Objects;
import java.util.UUID;

/**
 * DTO рекомендации банковского продукта.
 *
 * <p>Используется для передачи данных о рекомендации
 * между слоями приложения и в REST-ответе.
 */
public class RecommendationDto {
    /** Уникальный идентификатор продукта */
    private UUID productId;

    /** Название банковского продукта */
    private String productName;

    /** Текстовое описание рекомендации */
    private String productText;

    public RecommendationDto() {}
    public RecommendationDto(UUID productId, String productName, String productText) {
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
    }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    @Override
    public boolean equals(Object o) { return o instanceof RecommendationDto that &&
            Objects.equals(productId, that.productId); }
    @Override
    public int hashCode() { return Objects.hashCode(productId); }
}
