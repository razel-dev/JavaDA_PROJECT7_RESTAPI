package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;

import com.nnk.springboot.services.RuleNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RuleNameServiceImpl implements RuleNameService {

    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RuleNameDto> findAll() {
        long t0 = System.nanoTime();
        log.info("Liste des règles (RuleName) - démarrage");
        List<RuleNameDto> result = ruleNameRepository.findAll()
                .stream()
                .map(ruleNameMapper::toDto)
                .toList();
        log.info("Liste récupérée: {} élément(s) en {} ms", result.size(), (System.nanoTime() - t0) / 1_000_000);
        return result;
    }

    @Override
    public RuleNameDto create(RuleNameDto dto) {
        log.info("Création d'une règle: name='{}' description='{}'", dto.getName(), dto.getDescription());
        dto.setId(null);
        RuleName entity = ruleNameMapper.toEntity(dto);
        RuleName saved = ruleNameRepository.save(entity);
        log.info("Règle créée avec succès: id={}", saved.getId());
        return ruleNameMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RuleNameDto getRuleName(Integer id) {
        log.debug("Récupération de la règle id={}", id);
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        return ruleNameMapper.toDto(entity);
    }

    @Override
    public RuleNameDto update(Integer id, RuleNameDto dto) {
        log.info("Mise à jour de la règle id={} (name='{}' description='{}')", id, dto.getName(), dto.getDescription());
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Mise à jour impossible: règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        ruleNameMapper.updateEntity(entity, dto);
        RuleName saved = ruleNameRepository.save(entity);
        log.info("Règle mise à jour: id={}", saved.getId());
        return ruleNameMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        log.info("Suppression de la règle id={}", id);
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression impossible: règle introuvable pour id={}", id);
                    return new IllegalArgumentException("RuleName introuvable avec l'id " + id);
                });
        ruleNameRepository.delete(entity);
        log.info("Règle supprimée: id={}", id);
    }
}