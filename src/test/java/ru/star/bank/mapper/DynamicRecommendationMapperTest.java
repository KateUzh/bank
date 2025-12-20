package ru.star.bank.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.ArgumentsDto;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.dto.DynamicRuleDto;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DynamicRecommendationMapperTest {

    private DynamicRecommendationMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new DynamicRecommendationMapper();
    }

    @Test
    void testArgumentDtoToEntityAndBack() {
        ArgumentsDto dto = new ArgumentsDto();
        dto.setProductType("DEBIT");
        dto.setTransactionType("DEPOSIT");
        dto.setMathSign(">");
        dto.setThresholdSum(1000);

        ArgumentsEntity entity = mapper.argumentDtoToEntity(dto);
        assertEquals(dto.getProductType(), entity.getProductType());
        assertEquals(dto.getTransactionType(), entity.getTransactionType());
        assertEquals(dto.getMathSign(), entity.getMathSign());
        assertEquals(dto.getThresholdSum(), entity.getThresholdSum());

        ArgumentsDto convertedBack = mapper.argumentEntityToDto(entity);
        assertEquals(dto.getProductType(), convertedBack.getProductType());
        assertEquals(dto.getTransactionType(), convertedBack.getTransactionType());
        assertEquals(dto.getMathSign(), convertedBack.getMathSign());
        assertEquals(dto.getThresholdSum(), convertedBack.getThresholdSum());
    }

    @Test
    void testDynamicRuleDtoToEntityAndBack() {
        ArgumentsDto argDto = new ArgumentsDto();
        argDto.setProductType("DEBIT");
        argDto.setTransactionType("DEPOSIT");
        argDto.setMathSign(">");
        argDto.setThresholdSum(1000);

        DynamicRuleDto ruleDto = new DynamicRuleDto();
        ruleDto.setQuery("TRANSACTION_SUM_COMPARE");
        ruleDto.setArguments(Collections.singletonList(argDto));
        ruleDto.setNegate(false);

        DynamicRuleEntity entity = mapper.dynamicRuleDtoToEntity(ruleDto);
        assertEquals(ruleDto.getQuery(), entity.getQuery());
        assertEquals(ruleDto.isNegate(), entity.isNegate());
        assertEquals(ruleDto.getArguments().size(), entity.getArgumentsEntity().size());
        assertEquals(ruleDto.getArguments().get(0).getProductType(),
                entity.getArgumentsEntity().get(0).getProductType());

        DynamicRuleDto convertedBack = mapper.dynamicRuleEntityToDto(entity);
        assertEquals(ruleDto.getQuery(), convertedBack.getQuery());
        assertEquals(ruleDto.isNegate(), convertedBack.isNegate());
        assertEquals(ruleDto.getArguments().get(0).getProductType(),
                convertedBack.getArguments().get(0).getProductType());
    }

    @Test
    void testDynamicRecommendationDtoToEntityAndBack() {
        ArgumentsDto argDto = new ArgumentsDto();
        argDto.setProductType("DEBIT");
        argDto.setTransactionType("DEPOSIT");
        argDto.setMathSign(">");
        argDto.setThresholdSum(1000);

        DynamicRuleDto ruleDto = new DynamicRuleDto();
        ruleDto.setQuery("TRANSACTION_SUM_COMPARE");
        ruleDto.setArguments(Collections.singletonList(argDto));
        ruleDto.setNegate(false);

        DynamicRecommendationDto recDto = new DynamicRecommendationDto();
        recDto.setId(UUID.randomUUID());
        recDto.setProduct_name("Test Product");
        recDto.setProduct_id(UUID.randomUUID());
        recDto.setProduct_text("Test Text");
        recDto.setRule(Collections.singletonList(ruleDto));

        DynamicRecommendationEntity entity = mapper.dynamicRecommendationDtoToEntity(recDto);
        assertEquals(recDto.getProduct_name(), entity.getProductName());
        assertEquals(recDto.getProduct_id(), entity.getProductId());
        assertEquals(recDto.getProduct_text(), entity.getProductText());
        assertEquals(recDto.getRule().size(), entity.getRules().size());

        DynamicRecommendationDto convertedBack = mapper.dynamicRecommendationEntityToDto(entity);
        assertEquals(recDto.getProduct_name(), convertedBack.getProduct_name());
        assertEquals(recDto.getProduct_id(), convertedBack.getProduct_id());
        assertEquals(recDto.getProduct_text(), convertedBack.getProduct_text());
        assertEquals(recDto.getRule().size(), convertedBack.getRule().size());
        assertEquals(recDto.getRule().get(0).getQuery(),
                convertedBack.getRule().get(0).getQuery());
    }

    @Test
    void testMultipleRulesAndArguments() {
        ArgumentsDto arg1 = new ArgumentsDto();
        arg1.setProductType("DEBIT");

        ArgumentsDto arg2 = new ArgumentsDto();
        arg2.setProductType("CREDIT");

        DynamicRuleDto rule1 = new DynamicRuleDto();
        rule1.setQuery("USER_OF");
        rule1.setArguments(Collections.singletonList(arg1));
        rule1.setNegate(true);

        DynamicRuleDto rule2 = new DynamicRuleDto();
        rule2.setQuery("ACTIVE_USER_OF");
        rule2.setArguments(Collections.singletonList(arg2));
        rule2.setNegate(false);

        DynamicRecommendationDto recDto = new DynamicRecommendationDto();
        recDto.setProduct_name("Multi Rule Product");
        recDto.setProduct_id(UUID.randomUUID());
        recDto.setProduct_text("Multi Rule Text");
        recDto.setRule(Arrays.asList(rule1, rule2));

        DynamicRecommendationEntity entity = mapper.dynamicRecommendationDtoToEntity(recDto);
        assertEquals(2, entity.getRules().size());
        assertEquals(1, entity.getRules().get(0).getArgumentsEntity().size());
        assertEquals(1, entity.getRules().get(1).getArgumentsEntity().size());

        DynamicRecommendationDto convertedBack = mapper.dynamicRecommendationEntityToDto(entity);
        assertEquals(2, convertedBack.getRule().size());
        assertEquals("USER_OF", convertedBack.getRule().get(0).getQuery());
        assertEquals("ACTIVE_USER_OF", convertedBack.getRule().get(1).getQuery());
    }
}
