package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RuleNameMapper {
    RuleNameDto toDto(RuleName entity);
    RuleName toEntity(RuleNameDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(RuleName entity, RuleNameDto dto);

}