package ru.star.bank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "dynamic_recommendations")
public class DynamicRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_text")
    private String productText;

    @OneToMany(mappedBy = "dynamicRecommendation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DynamicRuleEntity> rules = new ArrayList<>();

    public DynamicRecommendationEntity() {}
    public DynamicRecommendationEntity(String productName, UUID productId, String productText, List<DynamicRuleEntity> rules) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rules = rules;
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }
    public List<DynamicRuleEntity> getRules() { return rules; }
    public void setRules(List<DynamicRuleEntity> rules) { this.rules = rules; }

    @Override
    public boolean equals(Object o) { return o instanceof DynamicRecommendationEntity that && Objects.equals(productId, that.productId); }
    @Override
    public int hashCode() { return Objects.hashCode(productId); }
}
