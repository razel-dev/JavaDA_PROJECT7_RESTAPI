package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;

import com.nnk.springboot.services.CurvePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CurvePointServiceImpl implements CurvePointService {
    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper curvePointMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CurvePointDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des points de courbe (CurvePoint) - démarrage");
        List<CurvePointDto> result = curvePointRepository.findAll()
                .stream()
                .map(curvePointMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    @Override
    public CurvePointDto create(CurvePointDto dto) {
        log.info("Création d'un point de courbe: curveId={} term={} value={}", dto.getCurveId(), dto.getTerm(), dto.getValue());
        CurvePoint entity = curvePointMapper.toEntity(dto);
        CurvePoint saved = curvePointRepository.save(entity);
        log.info("Point de courbe créé avec succès: id={}", saved.getId());
        return curvePointMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public CurvePointDto getCurvePoint(Integer id) {
        log.debug("Récupération du point de courbe id={}", id);
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Point de courbe introuvable pour id={}", id);
                    return new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
                });
        return curvePointMapper.toDto(entity);
    }

    @Override
    public CurvePointDto update(Integer id, CurvePointDto dto) {
        log.info("Mise à jour du point de courbe id={} (curveId={} term={} value={})", id, dto.getCurveId(), dto.getTerm(), dto.getValue());
        CurvePoint entity = curvePointRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: point de courbe introuvable pour id={}", id);
                    return new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
                });
        curvePointMapper.updateEntity(entity, dto);
        CurvePoint saved = curvePointRepository.save(entity);
        log.info("Point de courbe mis à jour: id={}", saved.getId());
        return curvePointMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        log.info("Suppression du point de courbe id={}", id);
        if (!curvePointRepository.existsById(id)) {
            log.warn("Suppression impossible: point de courbe introuvable pour id={}", id);
            throw new IllegalArgumentException("CurvePoint introuvable avec l'id " + id);
        }
        curvePointRepository.deleteById(id);
        log.info("Point de courbe supprimé: id={}", id);
    }
}
