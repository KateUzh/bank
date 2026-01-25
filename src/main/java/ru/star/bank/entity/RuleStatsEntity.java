package ru.star.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 * Сущность статистики срабатываний правил рекомендаций.
 *
 * <p>Хранит количество применений конкретного правила
 * (по его идентификатору) для последующего анализа и отчетности.
 *
 * <p><b>Поля:</b>
 * <ul>
 *     <li><b>ruleId</b> — уникальный идентификатор правила (идентификатор из системы правил);</li>
 *     <li><b>count</b> — количество срабатываний правила.</li>
 * </ul>
 *
 * <p><b>Особенности использования:</b>
 * <ul>
 *     <li>Поле <code>ruleId</code> является первичным ключом;</li>
 *     <li>Метод {@link #increment()} увеличивает счетчик на единицу — удобно для атомарного обновления в памяти;</li>
 *     <li>Сущность может использоваться как для хранения в базе, так и для кэширования статистики в памяти;</li>
 *     <li>Методы геттеров/сеттеров позволяют легко интегрироваться с JPA и DTO слоями.</li>
 * </ul>
 */
@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "count")
    private long count;

    public RuleStatsEntity() {}

    public RuleStatsEntity(String ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void increment() {
        this.count++;
    }
}
