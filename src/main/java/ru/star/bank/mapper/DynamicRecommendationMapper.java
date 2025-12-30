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
        ArgumentsEntity argumentsEntity = new ArgumentsEntity();
        argumentsEntity.setProductType(argumentsDto.getProductType());
        argumentsEntity.setDynamicRule(ruleEntity);

        if (argumentsDto.getTransactionType() != null) {
            argumentsEntity.setTransactionType(argumentsDto.getTransactionType());
        }
        if (argumentsDto.getMathSign() != null) {
            argumentsEntity.setMathSign(argumentsDto.getMathSign());
        }
        if (argumentsDto.getThresholdSum() != null) {
            argumentsEntity.setThresholdSum(argumentsDto.getThresholdSum());
        }
        return argumentsEntity;
    }

    public List<ArgumentsEntity> argumentsDtoToEntity(List<ArgumentsDto> argumentsDtoList, DynamicRuleEntity ruleEntity) {
        return argumentsDtoList.stream()
                .map(dto -> argumentDtoToEntity(dto, ruleEntity))
                .collect(Collectors.toList());
    }

    public DynamicRuleEntity dynamicRuleDtoToEntity(DynamicRuleDto ruleDto, DynamicRecommendationEntity entity) {
        DynamicRuleEntity ruleEntity = new DynamicRuleEntity();
        ruleEntity.setQuery(ruleDto.getQuery());
        ruleEntity.setNegate(ruleDto.isNegate());
        ruleEntity.setDynamicRecommendation(entity);

        if (ruleDto.getArguments() != null) {
            ruleEntity.setArgumentsEntity(argumentsDtoToEntity(ruleDto.getArguments(), ruleEntity));
        }
        return ruleEntity;
    }

    public List<DynamicRuleEntity> dynamicRulesDtoToEntity(List<DynamicRuleDto> dynamicRuleDtoList, DynamicRecommendationEntity entity) {
        return dynamicRuleDtoList.stream()
                .map(ruleDto -> dynamicRuleDtoToEntity(ruleDto, entity))
                .collect(Collectors.toList());
    }

    public DynamicRecommendationEntity dynamicRecommendationDtoToEntity(DynamicRecommendationDto dto) {
        DynamicRecommendationEntity entity = new DynamicRecommendationEntity();
        entity.setProductName(dto.getProduct_name());
        entity.setProductId(dto.getProduct_id());
        entity.setProductText(dto.getProduct_text());
        if (dto.getRule() != null) {
            entity.setRules(dynamicRulesDtoToEntity(dto.getRule(), entity));

        }
        return entity;
    }

    public ArgumentsDto argumentEntityToDto(ArgumentsEntity argumentsEntity) {
        ArgumentsDto argumentsDto = new ArgumentsDto();
        argumentsDto.setProductType(argumentsEntity.getProductType());
        if (argumentsEntity.getTransactionType() != null) {
            argumentsDto.setTransactionType(argumentsEntity.getTransactionType());
        }
        if (argumentsEntity.getMathSign() != null) {
            argumentsDto.setMathSign(argumentsEntity.getMathSign());
        }
        if (argumentsEntity.getThresholdSum() != null) {
            argumentsDto.setThresholdSum(argumentsEntity.getThresholdSum());
        }
        return argumentsDto;
    }

    public List<ArgumentsDto> argumentsEntityToDto(List<ArgumentsEntity> argumentsEntityList) {
        return argumentsEntityList.stream()
                .map(this::argumentEntityToDto)
                .collect(Collectors.toList());
    }

    public DynamicRuleDto dynamicRuleEntityToDto(DynamicRuleEntity dynamicRuleEntity) {
        DynamicRuleDto dynamicRuleDto = new DynamicRuleDto();
        dynamicRuleDto.setQuery(dynamicRuleEntity.getQuery());
        dynamicRuleDto.setArguments(argumentsEntityToDto(dynamicRuleEntity.getArgumentsEntity()));
        dynamicRuleDto.setNegate(dynamicRuleEntity.isNegate());
        return dynamicRuleDto;
    }

    public List<DynamicRuleDto> dynamicRulesEntityToDto(List<DynamicRuleEntity> dynamicRuleEntityList) {
        return dynamicRuleEntityList.stream()
                .map(this::dynamicRuleEntityToDto)
                .collect(Collectors.toList());
    }

    public DynamicRecommendationDto dynamicRecommendationEntityToDto(DynamicRecommendationEntity
                                                                             dynamicRecommendationEntity) {
        DynamicRecommendationDto dynamicRecommendationDto = new DynamicRecommendationDto();
        dynamicRecommendationDto.setId(dynamicRecommendationEntity.getId());
        dynamicRecommendationDto.setProduct_id(dynamicRecommendationEntity.getProductId());
        dynamicRecommendationDto.setProduct_name(dynamicRecommendationEntity.getProductName());
        dynamicRecommendationDto.setProduct_text(dynamicRecommendationEntity.getProductText());
        dynamicRecommendationDto.setRule(dynamicRulesEntityToDto(dynamicRecommendationEntity.getRules()));
        return dynamicRecommendationDto;
    }
}
