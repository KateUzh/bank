package ru.star.bank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "product_name")
    private String productName;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_text")
    private String productText;

    @Column(name = "rule")
    @OneToMany(mappedBy = "dynamicRecommendation", cascade = CascadeType.ALL)
    private List<DynamicRuleEntity> rules = new ArrayList<>();

    public DynamicRecommendationEntity(){}

    public DynamicRecommendationEntity(String productName, UUID productId, String productText,
                                       List<DynamicRuleEntity> rules) {
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rules = rules;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<DynamicRuleEntity> getRules() {
        return rules;
    }

    public void setRules(List<DynamicRuleEntity> rules) {
        this.rules = rules;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DynamicRecommendationEntity that)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }

    @Override
    public String toString() {
        return "DynamicRecommendationEntity{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", productText='" + productText + '\'' +
                ", rules=" + rules +
                '}';
    }
}
