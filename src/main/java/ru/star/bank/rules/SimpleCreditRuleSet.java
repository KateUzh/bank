package ru.star.bank.rules;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.RecommendationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public SimpleCreditRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

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
