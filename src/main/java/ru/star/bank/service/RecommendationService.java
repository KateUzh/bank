package ru.star.bank.service;

import org.springframework.stereotype.Service;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.RecommendationRuleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRecommendationRepository dynamicRepository;
    private final RecommendationRepository userRepository;

    public RecommendationService(
            List<RecommendationRuleSet> ruleSets,
            DynamicRecommendationRepository dynamicRepository,
            RecommendationRepository userRepository
    ) {
        this.ruleSets = ruleSets;
        this.dynamicRepository = dynamicRepository;
        this.userRepository = userRepository;
    }

    public RecommendationResponse getRecommendations(UUID userId) {

        List<RecommendationDto> fixedRecommendations = ruleSets.stream()
                .map(rule -> rule.apply(userId))
                .flatMap(Optional::stream)
                .toList();

        List<RecommendationDto> dynamicRecommendations = dynamicRepository.findAll().stream()
                .filter(ruleEntity -> checkDynamicRule(userId, ruleEntity))
                .map(entity -> new RecommendationDto(
                        entity.getProductId(),
                        entity.getProductName(),
                        entity.getProductText()
                ))
                .toList();

        List<RecommendationDto> all = new ArrayList<>();
        all.addAll(fixedRecommendations);
        all.addAll(dynamicRecommendations);

        return new RecommendationResponse(userId, all);
    }

    private boolean checkDynamicRule(UUID userId, DynamicRecommendationEntity ruleEntity) {
        for (DynamicRuleEntity rule : ruleEntity.getRules()) {
            if (!evaluateRule(userId, rule)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateRule(UUID userId, DynamicRuleEntity rule) {

        List<ArgumentsEntity> args = rule.getArgumentsEntity();
        boolean negate = rule.isNegate();

        boolean result = switch (rule.getQuery()) {

            case "USER_OF" -> {
                if (args.size() < 1) yield false;
                String productType = args.get(0).getProductType();
                yield userRepository.hasProductOfType(userId, productType);
            }

            case "ACTIVE_USER_OF" -> {
                if (args.size() < 1) yield false;
                String productType = args.get(0).getProductType();
                yield userRepository.getTransactionCount(userId, productType) >= 5;
            }

            case "TRANSACTION_SUM_COMPARE" -> {
                if (args.size() < 4) yield false;

                String productType = args.get(0).getProductType();
                String transactionType = args.get(1).getProductType();
                String operator = args.get(2).getProductType();
                int threshold = Integer.parseInt(args.get(3).getProductType());

                int sum = userRepository.getSumOfTransactions(userId, productType, transactionType);
                yield compare(sum, operator, threshold);
            }

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                if (args.size() < 2) yield false;

                String productType = args.get(0).getProductType();
                String operator = args.get(1).getProductType();

                int deposit = userRepository.getSumOfTransactions(userId, productType, "DEPOSIT");
                int withdraw = userRepository.getSumOfTransactions(userId, productType, "WITHDRAW");

                yield compare(deposit, operator, withdraw);
            }

            default -> false;
        };

        return negate ? !result : result;
    }

    private boolean compare(int left, String operator, int right) {
        return switch (operator) {
            case ">" -> left > right;
            case "<" -> left < right;
            case "=" -> left == right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;
            default -> false;
        };
    }
}
