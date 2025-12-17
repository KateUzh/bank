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

    public ArgumentsEntity argumentDtoToEntity(ArgumentsDto argumentsDto) {
        ArgumentsEntity argumentsEntity = new ArgumentsEntity();
        argumentsEntity.setProductType(argumentsDto.getProductType());
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

    public List<ArgumentsEntity> argumentsDtoToEntity(List<ArgumentsDto> argumentsDtoList) {
        return argumentsDtoList.stream()
                .map(this::argumentDtoToEntity)
                .collect(Collectors.toList());
    }

    public DynamicRuleEntity dynamicRuleDtoToEntity(DynamicRuleDto ruleDto) {
        DynamicRuleEntity ruleEntity = new DynamicRuleEntity();
        ruleEntity.setQuery(ruleDto.getQuery());
        ruleEntity.setArgumentsEntity(argumentsDtoToEntity(ruleDto.getArguments()));
        ruleEntity.setNegate(ruleDto.isNegate());
        return ruleEntity;
    }

    public List<DynamicRuleEntity> dynamicRulesDtoToEntity(List<DynamicRuleDto> dynamicRuleDtoList) {
        return dynamicRuleDtoList.stream()
                .map(this::dynamicRuleDtoToEntity)
                .collect(Collectors.toList());
    }

    public DynamicRecommendationEntity dynamicRecommendationDtoToEntity(DynamicRecommendationDto
                                                                                recommendationDto) {
        DynamicRecommendationEntity dynamicRecommendationEntity = new DynamicRecommendationEntity();
        dynamicRecommendationEntity.setProductName(recommendationDto.getProduct_name());
        dynamicRecommendationEntity.setProductId(recommendationDto.getProduct_id());
        dynamicRecommendationEntity.setProductText(recommendationDto.getProduct_text());

        if(recommendationDto.getRule() != null){
            List<DynamicRuleEntity> ruleEntityList = recommendationDto.getRule().stream()
                    .map(ruleDto -> {
                        DynamicRuleEntity ruleEntity = new DynamicRuleEntity();
                        ruleEntity.setQuery(ruleDto.getQuery());
                        ruleEntity.setArgumentsEntity(argumentsDtoToEntity(ruleDto.getArguments()));
                        ruleEntity.setNegate(ruleDto.isNegate());
                        ruleEntity.setDynamicRecommendation(dynamicRecommendationEntity);

                        if(ruleDto.getArguments() != null){
                            List<ArgumentsEntity> argumentsEntityList = ruleDto.getArguments().stream()
                                    .map(argumentsDto -> {
                                        ArgumentsEntity argumentsEntity = new ArgumentsEntity();
                                        argumentsEntity.setProductType(argumentsDto.getProductType());
                                        if (argumentsDto.getTransactionType() != null) {
                                            argumentsEntity.setTransactionType(argumentsDto.getTransactionType());
                                        }
                                        if (argumentsDto.getMathSign() != null) {
                                            argumentsEntity.setMathSign(argumentsDto.getMathSign());
                                        }
                                        if (argumentsDto.getThresholdSum() != null) {
                                            argumentsEntity.setThresholdSum(argumentsDto.getThresholdSum());
                                        }
                                        argumentsEntity.setDynamicRule(ruleEntity);

                                        return argumentsEntity;
                                    }).collect(Collectors.toList());
                            ruleEntity.setArgumentsEntity(argumentsEntityList);
                        }

                        return ruleEntity;
                    }).collect(Collectors.toList());
            dynamicRecommendationEntity.setRules(ruleEntityList);
        }
        return dynamicRecommendationEntity;
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
