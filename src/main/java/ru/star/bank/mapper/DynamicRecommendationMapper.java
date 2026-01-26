package ru.star.bank.mapper;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.ArgumentsDto;
import ru.star.bank.dto.DynamicRecommendationDto;
import ru.star.bank.dto.DynamicRuleDto;
import ru.star.bank.entity.ArgumentsEntity;
import ru.star.bank.entity.DynamicRecommendationEntity;
import ru.star.bank.entity.DynamicRuleEntity;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Маппер для преобразования динамических рекомендаций
 * между DTO и entity моделями.
 *
 * <p>Отвечает за корректную сборку иерархии объектов:
 * <ul>
 *     <li>{@link DynamicRecommendationEntity}</li>
 *     <li>{@link DynamicRuleEntity}</li>
 *     <li>{@link ArgumentsEntity}</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b>
 * <ul>
 *     <li>маппинг выполняется вручную без использования reflection;</li>
 *     <li>при преобразовании DTO → entity корректно
 *     проставляются обратные связи;</li>
 *     <li>методы допускают {@code null} на входе
 *     и возвращают {@code null} или пустые коллекции.</li>
 * </ul>
 */
@Component
public class DynamicRecommendationMapper {

    public ArgumentsEntity argumentDtoToEntity(ArgumentsDto dto, DynamicRuleEntity rule) {
        if (dto == null) return null;
        ArgumentsEntity entity = new ArgumentsEntity();
        entity.setProductType(dto.getProductType());
        entity.setTransactionType(dto.getTransactionType());
        entity.setMathSign(dto.getMathSign());
        entity.setThresholdSum(dto.getThresholdSum());
        entity.setDynamicRule(rule);
        return entity;
    }

    public List<ArgumentsEntity> argumentsDtoToEntity(List<ArgumentsDto> dtoList, DynamicRuleEntity rule) {
        if (dtoList == null || dtoList.isEmpty()) return List.of();
        return dtoList.stream().map(d -> argumentDtoToEntity(d, rule)).collect(Collectors.toList());
    }

    public DynamicRuleEntity dynamicRuleDtoToEntity(DynamicRuleDto dto, DynamicRecommendationEntity entity) {
        if (dto == null) return null;
        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setQuery(dto.getQuery());
        rule.setNegate(dto.isNegate());
        rule.setDynamicRecommendation(entity);
        rule.setArgumentsEntity(argumentsDtoToEntity(dto.getArguments(), rule));
        return rule;
    }

    public List<DynamicRuleEntity> dynamicRulesDtoToEntity(List<DynamicRuleDto> dtoList, DynamicRecommendationEntity entity) {
        if (dtoList == null || dtoList.isEmpty()) return List.of();
        return dtoList.stream().map(d -> dynamicRuleDtoToEntity(d, entity)).collect(Collectors.toList());
    }

    public DynamicRecommendationEntity dynamicRecommendationDtoToEntity(DynamicRecommendationDto dto) {
        if (dto == null) return null;
        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductId(dto.getProduct_id());
        entity.setProductName(dto.getProduct_name());
        entity.setProductText(dto.getProduct_text());
        entity.setRules(dynamicRulesDtoToEntity(dto.getRule(), entity));
        return entity;
    }

    public ArgumentsDto argumentEntityToDto(ArgumentsEntity entity) {
        if (entity == null) return null;
        ArgumentsDto dto = new ArgumentsDto();
        dto.setProductType(entity.getProductType());
        dto.setTransactionType(entity.getTransactionType());
        dto.setMathSign(entity.getMathSign());
        dto.setThresholdSum(entity.getThresholdSum());
        return dto;
    }

    public List<ArgumentsDto> argumentsEntityToDto(List<ArgumentsEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) return List.of();
        return entityList.stream().map(this::argumentEntityToDto).collect(Collectors.toList());
    }

    public DynamicRuleDto dynamicRuleEntityToDto(DynamicRuleEntity entity) {
        if (entity == null) return null;
        DynamicRuleDto dto = new DynamicRuleDto();
        dto.setQuery(entity.getQuery());
        dto.setNegate(entity.isNegate());
        dto.setArguments(argumentsEntityToDto(entity.getArgumentsEntity()));
        return dto;
    }

    public List<DynamicRuleDto> dynamicRulesEntityToDto(List<DynamicRuleEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) return List.of();
        return entityList.stream().map(this::dynamicRuleEntityToDto).collect(Collectors.toList());
    }

    public DynamicRecommendationDto dynamicRecommendationEntityToDto(DynamicRecommendationEntity entity) {
        if (entity == null) return null;
        DynamicRecommendationDto dto = new DynamicRecommendationDto();
        dto.setId(entity.getId());
        dto.setProduct_id(entity.getProductId());
        dto.setProduct_name(entity.getProductName());
        dto.setProduct_text(entity.getProductText());
        dto.setRule(dynamicRulesEntityToDto(entity.getRules()));
        return dto;
    }
}
