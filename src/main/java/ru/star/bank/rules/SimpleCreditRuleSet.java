package ru.star.bank.rules;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.RecommendationRepository;

import java.util.Optional;
import java.util.UUID;
/**
 * Фиксированное правило рекомендации продукта «Простой кредит».
 *
 * <p>Правило предназначено для пользователей, не имеющих
 * активных кредитных продуктов, но демонстрирующих
 * высокую платёжную активность по дебетовым операциям.
 *
 * <p><b>Условия применения рекомендации:</b>
 * <ul>
 *     <li>у пользователя отсутствует кредитный продукт;</li>
 *     <li>сумма пополнений по дебетовому продукту превышает
 *     сумму списаний;</li>
 *     <li>сумма списаний по дебетовому продукту превышает 100 000.</li>
 * </ul>
 *
 * <p>При выполнении всех условий возвращается рекомендация
 * продукта «Простой кредит».
 */
@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }
    /**
     * Применяет правило рекомендации к пользователю.
     *
     * <p>Метод выполняет проверку бизнес-условий и возвращает
     * рекомендацию только при их полном выполнении.
     *
     * @param userId идентификатор пользователя
     * @return {@link Optional} с рекомендацией продукта
     *         либо {@link Optional#empty()}, если условия не выполнены
     */
    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesCredit = repository.hasProductOfType(userId, "CREDIT");
        int debitDeposit = repository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT");
        int debitWithdraw = repository.getSumOfTransactions(userId, "DEBIT", "WITHDRAW");

        if (!usesCredit && debitDeposit > debitWithdraw && debitWithdraw > 100000) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                    "Простой кредит",
                    """
                            Откройте мир выгодных кредитов с нами! 
                            Низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту. 
                            Быстрое рассмотрение заявки и удобное оформление онлайн — всё для вашего комфорта.
                            """
            ));
        }
        return Optional.empty();
    }
}
