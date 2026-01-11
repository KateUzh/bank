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

@Component
public class DynamicRecommendationMapper {

    public ArgumentsEntity argumentDtoToEntity(ArgumentsDto argumentsDto, DynamicRuleEntity ruleEntity) {
        if (argumentsDto == null) return null;
        ArgumentsEntity argumentsEntity = new ArgumentsEntity();
        argumentsEntity.setProductType(argumentsDto.getProductType());
        argumentsEntity.setDynamicRule(ruleEntity);
        argumentsEntity.setTransactionType(argumentsDto.getTransactionType());
        argumentsEntity.setMathSign(argumentsDto.getMathSign());
        argumentsEntity.setThresholdSum(argumentsDto.getThresholdSum());
        return argumentsEntity;
    }

    public List<ArgumentsEntity> argumentsDtoToEntity(List<ArgumentsDto> argumentsDtoList, DynamicRuleEntity ruleEntity) {
        if (argumentsDtoList == null || argumentsDtoList.isEmpty()) return List.of();
        return argumentsDtoList.stream()
                .map(dto -> argumentDtoToEntity(dto, ruleEntity))
                .collect(Collectors.toList());
    }

    public DynamicRuleEntity dynamicRuleDtoToEntity(DynamicRuleDto ruleDto, DynamicRecommendationEntity entity) {
        if (ruleDto == null) return null;
        DynamicRuleEntity ruleEntity = new DynamicRuleEntity();
        ruleEntity.setQuery(ruleDto.getQuery());
        ruleEntity.setNegate(ruleDto.isNegate());
        ruleEntity.setDynamicRecommendation(entity);
        ruleEntity.setArgumentsEntity(argumentsDtoToEntity(ruleDto.getArguments(), ruleEntity));
        return ruleEntity;
    }

    public List<DynamicRuleEntity> dynamicRulesDtoToEntity(List<DynamicRuleDto> dynamicRuleDtoList, DynamicRecommendationEntity entity) {
        if (dynamicRuleDtoList == null || dynamicRuleDtoList.isEmpty()) return List.of();
        return dynamicRuleDtoList.stream()
                .map(ruleDto -> dynamicRuleDtoToEntity(ruleDto, entity))
                .collect(Collectors.toList());
    }

    public DynamicRecommendationEntity dynamicRecommendationDtoToEntity(DynamicRecommendationDto dto) {
        if (dto == null) return null;
        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductName(dto.getProduct_name());
        entity.setProductId(dto.getProduct_id());
        entity.setProductText(dto.getProduct_text());
        entity.setRules(dynamicRulesDtoToEntity(dto.getRule(), entity));
        return entity;
    }

    public ArgumentsDto argumentEntityToDto(ArgumentsEntity argumentsEntity) {
        if (argumentsEntity == null) return null;
        ArgumentsDto argumentsDto = new ArgumentsDto();
        argumentsDto.setProductType(argumentsEntity.getProductType());
        argumentsDto.setTransactionType(argumentsEntity.getTransactionType());
        argumentsDto.setMathSign(argumentsEntity.getMathSign());
        argumentsDto.setThresholdSum(argumentsEntity.getThresholdSum());
        return argumentsDto;
    }

    public List<ArgumentsDto> argumentsEntityToDto(List<ArgumentsEntity> argumentsEntityList) {
        if (argumentsEntityList == null || argumentsEntityList.isEmpty()) return List.of();
        return argumentsEntityList.stream()
                .map(this::argumentEntityToDto)
                .collect(Collectors.toList());
    }

    public DynamicRuleDto dynamicRuleEntityToDto(DynamicRuleEntity dynamicRuleEntity) {
        if (dynamicRuleEntity == null) return null;
        DynamicRuleDto dynamicRuleDto = new DynamicRuleDto();
        dynamicRuleDto.setQuery(dynamicRuleEntity.getQuery());
        dynamicRuleDto.setArguments(argumentsEntityToDto(dynamicRuleEntity.getArgumentsEntity()));
        dynamicRuleDto.setNegate(dynamicRuleEntity.isNegate());
        return dynamicRuleDto;
    }

    public List<DynamicRuleDto> dynamicRulesEntityToDto(List<DynamicRuleEntity> dynamicRuleEntityList) {
        if (dynamicRuleEntityList == null || dynamicRuleEntityList.isEmpty()) return List.of();
        return dynamicRuleEntityList.stream()
                .filter(rule -> rule != null)
                .map(this::dynamicRuleEntityToDto)
                .collect(Collectors.toList());
    }

    public DynamicRecommendationDto dynamicRecommendationEntityToDto(DynamicRecommendationEntity dynamicRecommendationEntity) {
        if (dynamicRecommendationEntity == null) return null;
        DynamicRecommendationDto dynamicRecommendationDto = new DynamicRecommendationDto();
        dynamicRecommendationDto.setId(dynamicRecommendationEntity.getId());
        dynamicRecommendationDto.setProduct_id(dynamicRecommendationEntity.getProductId());
        dynamicRecommendationDto.setProduct_name(dynamicRecommendationEntity.getProductName());
        dynamicRecommendationDto.setProduct_text(dynamicRecommendationEntity.getProductText());
        dynamicRecommendationDto.setRule(dynamicRulesEntityToDto(dynamicRecommendationEntity.getRules()));
        return dynamicRecommendationDto;
    }
}
