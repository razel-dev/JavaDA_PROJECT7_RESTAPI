package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;

public final class RuleNameMapper {
    private RuleNameMapper() {}

    public static RuleNameDto toDto(RuleName entity) {
        if (entity == null) return null;
        RuleNameDto dto = new RuleNameDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setJson(entity.getJson());
        dto.setTemplate(entity.getTemplate());
        dto.setSqlStr(entity.getSqlStr());
        dto.setSqlPart(entity.getSqlPart());
        return dto;
    }

    public static RuleName toEntity(RuleNameDto dto) {
        if (dto == null) return null;
        RuleName entity = new RuleName();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setJson(dto.getJson());
        entity.setTemplate(dto.getTemplate());
        entity.setSqlStr(dto.getSqlStr());
        entity.setSqlPart(dto.getSqlPart());
        return entity;
    }

    public static void updateEntity(RuleName entity, RuleNameDto dto) {
        if (entity == null || dto == null) return;
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setJson(dto.getJson());
        entity.setTemplate(dto.getTemplate());
        entity.setSqlStr(dto.getSqlStr());
        entity.setSqlPart(dto.getSqlPart());
    }
}