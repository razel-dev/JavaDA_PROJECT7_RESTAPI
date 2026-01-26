package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;

public final class RatingMapper {
    private RatingMapper() {}

    public static RatingDto toDto(Rating entity) {
        if (entity == null) return null;
        RatingDto dto = new RatingDto();
        dto.setId(entity.getId());
        dto.setMoodysRating(entity.getMoodysRating());
        dto.setSandpRating(entity.getSandpRating());
        dto.setFitchRating(entity.getFitchRating());
        dto.setOrderNumber(entity.getOrderNumber());
        return dto;
    }

    public static Rating toEntity(RatingDto dto) {
        if (dto == null) return null;
        Rating entity = new Rating();
        entity.setId(dto.getId());
        entity.setMoodysRating(dto.getMoodysRating());
        entity.setSandpRating(dto.getSandpRating());
        entity.setFitchRating(dto.getFitchRating());
        entity.setOrderNumber(dto.getOrderNumber());
        return entity;
    }

    public static void updateEntity(Rating entity, RatingDto dto) {
        if (entity == null || dto == null) return;
        entity.setMoodysRating(dto.getMoodysRating());
        entity.setSandpRating(dto.getSandpRating());
        entity.setFitchRating(dto.getFitchRating());
        entity.setOrderNumber(dto.getOrderNumber());
    }
}