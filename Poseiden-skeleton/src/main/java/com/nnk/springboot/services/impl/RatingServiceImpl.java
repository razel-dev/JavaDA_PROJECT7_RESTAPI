package com.nnk.springboot.services.impl;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import com.nnk.springboot.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    public List<RatingDto> findAll() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDto)
                .toList();
    }

@Transactional(readOnly = true)
    @Override
    public RatingDto getRating(Integer id) {
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating introuvable pour l'id=" + id));
        return ratingMapper.toDto(entity);
    }

    @Override
    @Transactional
    public RatingDto create(RatingDto dto) {
        Rating entity = ratingMapper.toEntity(dto);
        Rating saved = ratingRepository.save(entity);
        return ratingMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RatingDto update(Integer id, RatingDto dto) {
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating introuvable pour l'id=" + id));
        ratingMapper.updateEntity(entity, dto);
        Rating saved = ratingRepository.save(entity);
        return ratingMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!ratingRepository.existsById(id)) {
            throw new EntityNotFoundException("Rating introuvable pour l'id=" + id);
        }
        ratingRepository.deleteById(id);
    }
}