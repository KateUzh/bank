package ru.star.bank.dto;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class DynamicRecommendationDto {
    private UUID id;
    private UUID product_id;
    private String product_name;
    private String product_text;
    private List<DynamicRuleDto> rule;

    public DynamicRecommendationDto() {}
    public DynamicRecommendationDto(String product_name, UUID product_id, String product_text, List<DynamicRuleDto> rule) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_text = product_text;
        this.rule = rule;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProduct_id() { return product_id; }
    public void setProduct_id(UUID product_id) { this.product_id = product_id; }
    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }
    public String getProduct_text() { return product_text; }
    public void setProduct_text(String product_text) { this.product_text = product_text; }
    public List<DynamicRuleDto> getRule() { return rule; }
    public void setRule(List<DynamicRuleDto> rule) { this.rule = rule; }

    @Override
    public boolean equals(Object o) { return o instanceof DynamicRecommendationDto that &&
            Objects.equals(product_id, that.product_id); }
    @Override
    public int hashCode() { return Objects.hashCode(product_id); }
}
