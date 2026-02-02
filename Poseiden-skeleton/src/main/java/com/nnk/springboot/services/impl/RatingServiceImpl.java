package com.nnk.springboot.services.impl;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import com.nnk.springboot.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    public List<RatingDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des notations (Rating) - démarrage");
        List<RatingDto> result = ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public RatingDto getRating(Integer id) {
        log.debug("Récupération de la notation id={}", id);
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notation introuvable pour id={}", id);
                    return new EntityNotFoundException("Rating introuvable pour l'id=" + id);
                });
        return ratingMapper.toDto(entity);
    }

    @Override
    @Transactional
    public RatingDto create(RatingDto dto) {
        log.info("Création d'une notation: moodys='{}' sandp='{}' fitch='{}' ordre={}",
                dto.getMoodysRating(), dto.getSandpRating(), dto.getFitchRating(), dto.getOrderNumber());
        Rating entity = ratingMapper.toEntity(dto);
        Rating saved = ratingRepository.save(entity);
        log.info("Notation créée avec succès: id={}", saved.getId());
        return ratingMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RatingDto update(Integer id, RatingDto dto) {
        log.info("Mise à jour de la notation id={} (moodys='{}' sandp='{}' fitch='{}' ordre={})",
                id, dto.getMoodysRating(), dto.getSandpRating(), dto.getFitchRating(), dto.getOrderNumber());
        Rating entity = ratingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: notation introuvable pour id={}", id);
                    return new EntityNotFoundException("Rating introuvable pour l'id=" + id);
                });
        ratingMapper.updateEntity(entity, dto);
        Rating saved = ratingRepository.save(entity);
        log.info("Notation mise à jour: id={}", saved.getId());
        return ratingMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Suppression de la notation id={}", id);
        if (!ratingRepository.existsById(id)) {
            log.warn("Suppression impossible: notation introuvable pour id={}", id);
            throw new EntityNotFoundException("Rating introuvable pour l'id=" + id);
        }
        ratingRepository.deleteById(id);
        log.info("Notation supprimée: id={}", id);
    }
}