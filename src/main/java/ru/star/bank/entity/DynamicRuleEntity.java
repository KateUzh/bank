package ru.star.bank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.star.bank.rules.QueryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "query", nullable = false)
    private QueryType query;

    @Column(name = "argumentsEntity")
    @OneToMany(mappedBy = "dynamicRule",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<ArgumentsEntity> argumentsEntity = new ArrayList<>();

    @Column(name = "negate")
    private boolean negate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynamicRecommendation_id", nullable = false)
    private DynamicRecommendationEntity dynamicRecommendation;

    public DynamicRuleEntity() {
    }

    public DynamicRuleEntity(QueryType query, List<ArgumentsEntity> argumentsEntity, boolean negate) {
        this.query = query;
        this.argumentsEntity = argumentsEntity;
        this.negate = negate;
    }

    public QueryType getQuery() {
        return query;
    }

    public void setQuery(QueryType query) {
        this.query = query;
    }

    public List<ArgumentsEntity> getArgumentsEntity() {
        return argumentsEntity;
    }

    public void setArgumentsEntity(List<ArgumentsEntity> argumentsEntity) {
        this.argumentsEntity = argumentsEntity;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRecommendationEntity getDynamicRecommendation() {
        return dynamicRecommendation;
    }

    public void setDynamicRecommendation(DynamicRecommendationEntity dynamicRecommendation) {
        this.dynamicRecommendation = dynamicRecommendation;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DynamicRuleEntity that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DynamicRuleEntity{" +
                "query='" + query + '\'' +
                ", argumentsEntity=" + argumentsEntity +
                ", negate=" + negate +
                '}';
    }
}
