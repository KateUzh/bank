package ru.star.bank.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.star.bank.dto.ArgumentsDto;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.dto.DynamicRuleDto;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;
import ru.star.bank.rules.QueryType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Юнит-тест для {@link DynamicRecommendationMapper}.
 *
 * <p>Проверяет корректность преобразования между DTO и Entity:
 * <ul>
 *     <li>{@link ArgumentsDto} ↔ {@link ArgumentsEntity}</li>
 *     <li>{@link DynamicRuleDto} ↔ {@link DynamicRuleEntity}</li>
 *     <li>{@link DynamicRecommendationDto} ↔ {@link DynamicRecommendationEntity}</li>
 * </ul>
 *
 * <p>Тесты обеспечивают:
 * <ul>
 *     <li>сохранение всех полей при преобразовании туда и обратно;</li>
 *     <li>правильную работу связей между сущностями (например, {@link DynamicRuleEntity} и {@link DynamicRecommendationEntity});</li>
 *     <li>поддержание списков аргументов и правил при маппинге.</li>
 * </ul>
 * */
public class DynamicRecommendationMapperTest {

    private DynamicRecommendationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DynamicRecommendationMapper();
    }

    @Test
    void testArgumentDtoToEntity_andBack() {
        DynamicRuleEntity ruleEntity = new DynamicRuleEntity();
        ArgumentsDto dto = new ArgumentsDto("DEBIT", "DEPOSIT", ">", 1000);
        ArgumentsEntity entity = mapper.argumentDtoToEntity(dto, ruleEntity);

        assertEquals("DEBIT", entity.getProductType());
        assertEquals("DEPOSIT", entity.getTransactionType());
        assertEquals(">", entity.getMathSign());
        assertEquals(1000, entity.getThresholdSum());

        ArgumentsDto backDto = mapper.argumentEntityToDto(entity);
        assertEquals(dto, backDto);
    }

    @Test
    void testDynamicRuleDtoToEntity_andBack() {
        DynamicRecommendationEntity recEntity = new DynamicRecommendationEntity();
        ArgumentsDto arg = new ArgumentsDto("DEBIT", "DEPOSIT", ">", 1000);
        DynamicRuleDto ruleDto = new DynamicRuleDto(QueryType.TRANSACTION_SUM_COMPARE, List.of(arg), true);

        DynamicRuleEntity entity = mapper.dynamicRuleDtoToEntity(ruleDto, recEntity);
        assertEquals(QueryType.TRANSACTION_SUM_COMPARE, entity.getQuery());
        assertTrue(entity.isNegate());
        assertEquals(1, entity.getArgumentsEntity().size());

        DynamicRuleDto backDto = mapper.dynamicRuleEntityToDto(entity);
        assertEquals(ruleDto.getQuery(), backDto.getQuery());
        assertEquals(ruleDto.isNegate(), backDto.isNegate());
        assertEquals(ruleDto.getArguments(), backDto.getArguments());
    }

    @Test
    void testDynamicRecommendationDtoToEntity_andBack() {
        ArgumentsDto arg = new ArgumentsDto("DEBIT", "DEPOSIT", ">", 1000);
        DynamicRuleDto ruleDto = new DynamicRuleDto(QueryType.TRANSACTION_SUM_COMPARE, List.of(arg), false);
        DynamicRecommendationDto dto = new DynamicRecommendationDto("TopSaving", null, "Text", List.of(ruleDto));

        DynamicRecommendationEntity entity = mapper.dynamicRecommendationDtoToEntity(dto);
        assertEquals("TopSaving", entity.getProductName());
        assertEquals(1, entity.getRules().size());

        DynamicRecommendationDto backDto = mapper.dynamicRecommendationEntityToDto(entity);
        assertEquals(dto.getProduct_name(), backDto.getProduct_name());
        assertEquals(dto.getRule().size(), backDto.getRule().size());
    }
}
