package ru.star.bank.rules;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.RecommendationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public TopSavingRuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesDebit = repository.hasProductOfType(userId, "DEBIT");
        int debitDeposit = repository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT");
        int savingDeposit = repository.getSumOfTransactions(userId, "SAVING", "DEPOSIT");
        int debitWithdraw = repository.getSumOfTransactions(userId, "DEBIT", "WITHDRAW");

        boolean sumCondition = debitDeposit >= 50000 || savingDeposit >= 50000;
        boolean profitCondition = debitDeposit > debitWithdraw;

        if (usesDebit && sumCondition && profitCondition) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                    "Top Saving",
                    """
                            Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский 
                            инструмент, 
                            который поможет вам легко и удобно накапливать деньги на важные цели. 
                            Накопление средств на конкретные цели, прозрачность, контроль и безопасность — всё под 
                            контролем.
                            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!
                            """
            ));
        }
        return Optional.empty();
    }
}
