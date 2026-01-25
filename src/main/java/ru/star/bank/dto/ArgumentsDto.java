package ru.star.bank.dto;

import java.util.Objects;
/**
 * DTO для аргументов динамического правила.
 *
 * <p>Используется для передачи данных между слоями приложения
 * (из REST API в сервис или для маппинга с {@link ru.star.bank.entity.ArgumentsEntity}).
 *
 * <p><b>Поля:</b>
 * <ul>
 *     <li><b>productType</b> — тип продукта (DEBIT, CREDIT, SAVING и т.д.);</li>
 *     <li><b>transactionType</b> — тип транзакции (DEPOSIT, WITHDRAW и т.д.);</li>
 *     <li><b>mathSign</b> — оператор сравнения (>, <, =, >=, <=);</li>
 *     <li><b>thresholdSum</b> — пороговое значение для сравнения;</li>
 * </ul>
 */
public class ArgumentsDto {
    private String productType;
    private String transactionType;
    private String mathSign;
    private Integer thresholdSum;

    public ArgumentsDto() {}
    public ArgumentsDto(String productType, String transactionType, String mathSign, Integer thresholdSum) {
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

    @Override
    public boolean equals(Object o) { return o instanceof ArgumentsDto that &&
            Objects.equals(productType, that.productType) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(mathSign, that.mathSign) &&
            Objects.equals(thresholdSum, that.thresholdSum); }

    @Override
    public int hashCode() { return Objects.hash(productType, transactionType, mathSign, thresholdSum); }
}
