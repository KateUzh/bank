package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.RecommendationResponse;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;
import ru.star.bank.repository.DynamicRecommendationRepository;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.rules.RecommendationRuleSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecommendationServiceDynamicTest {

    private DynamicRecommendationRepository dynamicRepository;
    private RecommendationRepository userRepository;
    private RecommendationRuleSet ruleSet;
    private RecommendationService service;
    private UUID userId;

    @BeforeEach
    void setup() {
        dynamicRepository = mock(DynamicRecommendationRepository.class);
        userRepository = mock(RecommendationRepository.class);
        ruleSet = mock(RecommendationRuleSet.class);
        userId = UUID.randomUUID();

        service = new RecommendationService(
                Collections.singletonList(ruleSet),
                dynamicRepository,
                userRepository
        );
    }

    @Test
    void testUserOfRule() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("CREDIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("USER_OF");
        rule.setArgumentsEntity(Collections.singletonList(arg1));
        rule.setNegate(false);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("User Product");
        dynRec.setProductText("User Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.hasProductOfType(userId, "CREDIT")).thenReturn(true);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("User Product", response.getRecommendations().get(0).getName());
    }

    @Test
    void testUserOfRuleNegate() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("CREDIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("USER_OF");
        rule.setArgumentsEntity(Collections.singletonList(arg1));
        rule.setNegate(true);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Negate User Product");
        dynRec.setProductText("Negate User Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.hasProductOfType(userId, "CREDIT")).thenReturn(true);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(0, response.getRecommendations().size());
    }

    @Test
    void testActiveUserOfRule() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("ACTIVE_USER_OF");
        rule.setArgumentsEntity(Collections.singletonList(arg1));
        rule.setNegate(false);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Active Product");
        dynRec.setProductText("Active Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getTransactionCount(userId, "DEBIT")).thenReturn(5);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("Active Product", response.getRecommendations().get(0).getName());
    }

    @Test
    void testActiveUserOfRuleNegate() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("ACTIVE_USER_OF");
        rule.setArgumentsEntity(Collections.singletonList(arg1));
        rule.setNegate(true);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Negate Active Product");
        dynRec.setProductText("Negate Active Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getTransactionCount(userId, "DEBIT")).thenReturn(5);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(0, response.getRecommendations().size());
    }

    @Test
    void testTransactionSumCompareRule() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");
        ArgumentsEntity arg2 = new ArgumentsEntity();
        arg2.setTransactionType("DEPOSIT");
        ArgumentsEntity arg3 = new ArgumentsEntity();
        arg3.setMathSign(">");
        ArgumentsEntity arg4 = new ArgumentsEntity();
        arg4.setThresholdSum(1000);

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("TRANSACTION_SUM_COMPARE");
        rule.setArgumentsEntity(Arrays.asList(arg1, arg2, arg3, arg4));
        rule.setNegate(false);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Sum Compare Product");
        dynRec.setProductText("Sum Compare Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT")).thenReturn(2000);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("Sum Compare Product", response.getRecommendations().get(0).getName());
    }

    @Test
    void testTransactionSumCompareRuleNegate() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");
        ArgumentsEntity arg2 = new ArgumentsEntity();
        arg2.setTransactionType("DEPOSIT");
        ArgumentsEntity arg3 = new ArgumentsEntity();
        arg3.setMathSign(">");
        ArgumentsEntity arg4 = new ArgumentsEntity();
        arg4.setThresholdSum(1000);

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("TRANSACTION_SUM_COMPARE");
        rule.setArgumentsEntity(Arrays.asList(arg1, arg2, arg3, arg4));
        rule.setNegate(true);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Negate Sum Compare Product");
        dynRec.setProductText("Negate Sum Compare Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT")).thenReturn(2000);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(0, response.getRecommendations().size());
    }

    @Test
    void testTransactionSumCompareDepositWithdrawRule() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");
        ArgumentsEntity arg2 = new ArgumentsEntity();
        arg2.setMathSign(">");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        rule.setArgumentsEntity(Arrays.asList(arg1, arg2));
        rule.setNegate(false);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Deposit vs Withdraw");
        dynRec.setProductText("Deposit vs Withdraw Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT")).thenReturn(2000);
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "WITHDRAW")).thenReturn(1000);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals("Deposit vs Withdraw", response.getRecommendations().get(0).getName());
    }

    @Test
    void testTransactionSumCompareDepositWithdrawRuleNegate() {
        ArgumentsEntity arg1 = new ArgumentsEntity();
        arg1.setProductType("DEBIT");
        ArgumentsEntity arg2 = new ArgumentsEntity();
        arg2.setMathSign(">");

        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
        rule.setArgumentsEntity(Arrays.asList(arg1, arg2));
        rule.setNegate(true);

        DynamicRecommendationEntity dynRec = new DynamicRecommendationEntity();
        dynRec.setProductId(UUID.randomUUID());
        dynRec.setProductName("Negate Deposit vs Withdraw");
        dynRec.setProductText("Negate Deposit vs Withdraw Text");
        dynRec.setRules(Collections.singletonList(rule));

        when(dynamicRepository.findAll()).thenReturn(Collections.singletonList(dynRec));
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "DEPOSIT")).thenReturn(2000);
        when(userRepository.getSumOfTransactions(userId, "DEBIT", "WITHDRAW")).thenReturn(1000);

        RecommendationResponse response = service.getRecommendations(userId);

        assertEquals(0, response.getRecommendations().size());
    }
}
