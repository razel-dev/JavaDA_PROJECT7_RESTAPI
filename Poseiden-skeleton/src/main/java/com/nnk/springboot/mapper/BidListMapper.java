package com.nnk.springboot.mapper;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface BidListMapper {
    BidListDto toDto(BidList entity);
    BidList toEntity(BidListDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget BidList entity, BidListDto dto);
}