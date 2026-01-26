package com.nnk.springboot.services;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {

    private final RatingRepository repository;
    private final RatingMapper mapper;

    @Override
    public List<RatingDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

@Transactional(readOnly = true)
    @Override
    public RatingDto getDto(Integer id) {
        Rating entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating introuvable pour l'id=" + id));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public RatingDto create(RatingDto dto) {
        Rating entity = mapper.toEntity(dto);
        Rating saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RatingDto update(Integer id, RatingDto dto) {
        Rating entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating introuvable pour l'id=" + id));
        mapper.updateEntity(entity, dto);
        Rating saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Rating introuvable pour l'id=" + id);
        }
        repository.deleteById(id);
    }
}