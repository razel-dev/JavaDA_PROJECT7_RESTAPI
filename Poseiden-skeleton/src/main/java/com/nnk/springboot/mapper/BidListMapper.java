package com.nnk.springboot.mapper;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;

public final class BidListMapper {
    private BidListMapper() {}

    public static BidListDto toDto(BidList entity) {
        if (entity == null) return null;
        BidListDto dto = new BidListDto();
        dto.setId(entity.getId());
        dto.setAccount(entity.getAccount());
        dto.setType(entity.getType());
        dto.setBidQuantity(entity.getBidQuantity());
        return dto;
    }

    public static BidList toEntity(BidListDto dto) {
        if (dto == null) return null;
        BidList entity = new BidList();
        entity.setId(dto.getId());
        entity.setAccount(dto.getAccount());
        entity.setType(dto.getType());
        entity.setBidQuantity(dto.getBidQuantity());
        return entity;
    }

    public static void updateEntity(BidList entity, BidListDto dto) {
        if (entity == null || dto == null) return;
        entity.setAccount(dto.getAccount());
        entity.setType(dto.getType());
        entity.setBidQuantity(dto.getBidQuantity());
    }
}