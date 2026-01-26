package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;

public final class CurvePointMapper {
    private CurvePointMapper() {}

    public static CurvePointDto toDto(CurvePoint entity) {
        if (entity == null) return null;
        CurvePointDto dto = new CurvePointDto();
        dto.setId(entity.getId());
        dto.setCurveId(entity.getCurveId());
        dto.setAsOfDate(entity.getAsOfDate());
        dto.setTerm(entity.getTerm());
        dto.setValue(entity.getValue());
        dto.setCreationDate(entity.getCreationDate());
        return dto;
    }

    public static CurvePoint toEntity(CurvePointDto dto) {
        if (dto == null) return null;
        CurvePoint entity = new CurvePoint();
        entity.setId(dto.getId());
        entity.setCurveId(dto.getCurveId());
        entity.setAsOfDate(dto.getAsOfDate());
        entity.setTerm(dto.getTerm());
        entity.setValue(dto.getValue());
        entity.setCreationDate(dto.getCreationDate());
        return entity;
    }

    public static void updateEntity(CurvePoint entity, CurvePointDto dto) {
        if (entity == null || dto == null) return;
        entity.setCurveId(dto.getCurveId());
        entity.setAsOfDate(dto.getAsOfDate());
        entity.setTerm(dto.getTerm());
        entity.setValue(dto.getValue());

    }
}