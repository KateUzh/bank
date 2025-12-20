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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRecommendationRepository dynamicRepository;
    private final RecommendationRepository userRepository;

    public RecommendationService(List<RecommendationRuleSet> ruleSets,
                                 DynamicRecommendationRepository dynamicRepository,
                                 RecommendationRepository userRepository) {
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

        List<RecommendationDto> allRecommendations = new ArrayList<>();
        allRecommendations.addAll(fixedRecommendations);
        allRecommendations.addAll(dynamicRecommendations);

        return new RecommendationResponse(userId, allRecommendations);
    }

    private boolean checkDynamicRule(UUID userId, DynamicRecommendationEntity ruleEntity) {
        for (DynamicRuleEntity rule : ruleEntity.getRules()) {
            boolean result = evaluateRule(userId, rule);
            if (!result) return false;
        }
        return true;
    }

    private boolean evaluateRule(UUID userId, DynamicRuleEntity rule) {
        String query = rule.getQuery();
        List<ArgumentsEntity> args = rule.getArgumentsEntity();
        boolean negate = rule.isNegate();

        boolean result = switch (query) {
            case "USER_OF" -> {
                String productType = args.get(0).getProductType();
                yield userRepository.hasProductOfType(userId, productType);
            }
            case "ACTIVE_USER_OF" -> {
                String productType = args.get(0).getProductType();
                int txCount = userRepository.getTransactionCount(userId, productType);
                yield txCount >= 5;
            }
            case "TRANSACTION_SUM_COMPARE" -> {
                String productType = args.get(0).getProductType();
                String transactionType = args.get(1).getTransactionType();
                String operator = args.get(2).getMathSign();
                int threshold = args.get(3).getThresholdSum();
                int sum = userRepository.getSumOfTransactions(userId, productType, transactionType);
                yield compare(sum, operator, threshold);
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                String productType = args.get(0).getProductType();
                String operator = args.get(1).getMathSign();
                int deposit = userRepository.getSumOfTransactions(userId, productType, "DEPOSIT");
                int withdraw = userRepository.getSumOfTransactions(userId, productType, "WITHDRAW");
                yield compare(deposit, operator, withdraw);
            }
            default -> false;
        };

        return negate ? !result : result;
    }

    private boolean compare(int a, String operator, int b) {
        return switch (operator) {
            case ">" -> a > b;
            case "<" -> a < b;
            case "=" -> a == b;
            case ">=" -> a >= b;
            case "<=" -> a <= b;
            default -> false;
        };
    }
}
