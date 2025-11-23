package ru.star.bank.rules;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.RecommendationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationRepository repository;

    public Invest500RuleSet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesDebit = repository.hasProductOfType(userId, "DEBIT");
        boolean usesInvest = repository.hasProductOfType(userId, "INVEST");
        int savingDepositSum = repository.getSumOfTransactions(userId, "SAVING", "DEPOSIT");

        if (usesDebit && !usesInvest && savingDepositSum > 1000) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                    "Invest 500",
                    """
                            Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! 
                            Воспользуйтесь налоговыми льготами и начните инвестировать с умом. 
                            Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом 
                            периоде. 
                            Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными 
                            рыночными тенденциями. 
                            Откройте ИИС сегодня и станьте ближе к финансовой независимости!
                            """
            ));
        }
        return Optional.empty();
    }
}
