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
/**
 * Сущность аргумента динамического правила.
 *
 * <p>Предназначена для хранения входных параметров
 * динамических правил рекомендаций банковских продуктов.
 *
 * <p><b>Поля:</b>
 * <ul>
 *     <li><b>productType</b> — тип продукта (DEBIT, CREDIT, SAVING и т.д.);</li>
 *     <li><b>transactionType</b> — тип транзакции (DEPOSIT, WITHDRAW и т.д.);</li>
 *     <li><b>mathSign</b> — оператор сравнения для правила (>, <, =, >=, <=);</li>
 *     <li><b>thresholdSum</b> — пороговое значение для сравнения;</li>
 *     <li><b>dynamicRule</b> — родительское динамическое правило.</li>
 * </ul>
 *
 * <p><b>Особенности JPA:</b>
 * <ul>
 *     <li>Используется `@ManyToOne` с `FetchType.LAZY` для динамического правила.</li>
 *     <li>Поле `dynamicRule` обязательно (`nullable = false`).</li>
 *     <li>Идентификатор `id` генерируется автоматически.</li>
 *     <li>Методы `equals` и `hashCode` основаны на `id` для корректной работы в коллекциях и Hibernate.</li>
 * </ul>
 */
@Entity
@Table(name = "arguments")
public class ArgumentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "dynamic_rule_id", nullable = false)
    private DynamicRuleEntity dynamicRule;

    public ArgumentsEntity() {}

    public ArgumentsEntity(String productType, String transactionType, String mathSign, Integer thresholdSum) {
        this.productType = productType;
        this.transactionType = transactionType;
        this.mathSign = mathSign;
        this.thresholdSum = thresholdSum;
    }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public String getMathSign() { return mathSign; }
    public void setMathSign(String mathSign) { this.mathSign = mathSign; }
    public Integer getThresholdSum() { return thresholdSum; }
    public void setThresholdSum(Integer thresholdSum) { this.thresholdSum = thresholdSum; }
    public DynamicRuleEntity getDynamicRule() { return dynamicRule; }
    public void setDynamicRule(DynamicRuleEntity dynamicRule) { this.dynamicRule = dynamicRule; }

    @Override
    public boolean equals(Object o) { return o instanceof ArgumentsEntity that && id == that.id; }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
