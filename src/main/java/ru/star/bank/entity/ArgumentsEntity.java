package ru.star.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "arguments")
public class ArgumentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "math_sign")
    private String mathSign;

    @Column(name = "threshold_sum")
    private Integer thresholdSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynamicRule_id", nullable = false)
    private DynamicRuleEntity dynamicRule;

    public ArgumentsEntity() {
    }

    public ArgumentsEntity(String productType, String transactionType, String mathSign, Integer thresholdSum) {
        this.productType = productType;
        this.transactionType = transactionType;
        this.mathSign = mathSign;
        this.thresholdSum = thresholdSum;
    }

    public ArgumentsEntity(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMathSign() {
        return mathSign;
    }

    public void setMathSign(String mathSign) {
        this.mathSign = mathSign;
    }

    public Integer getThresholdSum() {
        return thresholdSum;
    }

    public void setThresholdSum(Integer thresholdSum) {
        this.thresholdSum = thresholdSum;
    }

    public DynamicRuleEntity getDynamicRule() {
        return dynamicRule;
    }

    public void setDynamicRule(DynamicRuleEntity dynamicRule) {
        this.dynamicRule = dynamicRule;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArgumentsEntity that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ArgumentsEntity{" +
                "productType='" + productType + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", mathSign='" + mathSign + '\'' +
                ", thresholdSum=" + thresholdSum +
                '}';
    }
}
