package ru.star.bank.entity;

import jakarta.persistence.*;
import ru.star.bank.rules.QueryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueryType query;

    @OneToMany(mappedBy = "dynamicRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ArgumentsEntity> argumentsEntity = new ArrayList<>();

    @Column
    private boolean negate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynamic_recommendation_id", nullable = false)
    private DynamicRecommendationEntity dynamicRecommendation;

    public DynamicRuleEntity() {
    }

    public DynamicRuleEntity(QueryType query, List<ArgumentsEntity> argumentsEntity, boolean negate) {
        this.query = query;
        this.argumentsEntity = argumentsEntity;
        this.negate = negate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return o instanceof DynamicRuleEntity that && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
