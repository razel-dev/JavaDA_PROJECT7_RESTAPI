package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")


public interface TradeMapper {
    TradeDto toDto(Trade entity);
    Trade toEntity(TradeDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Trade entity, TradeDto dto);
    }