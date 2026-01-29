package com.nnk.springboot.services;

import com.nnk.springboot.dto.RatingDto;
import java.util.List;

public interface RatingService {
    List<RatingDto> findAll();

    RatingDto create(RatingDto dto);
    RatingDto update(Integer id, RatingDto dto);
    void delete(Integer id);
    RatingDto getRating(Integer id);
}