package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RuleNameServiceImpl implements RuleNameService {

    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RuleNameDto> findAll() {
        return ruleNameRepository.findAll()
                .stream()
                .map(ruleNameMapper::toDto)
                .toList();
    }

    @Override
    public RuleNameDto create(RuleNameDto dto) {
        dto.setId(null);
        RuleName entity = ruleNameMapper.toEntity(dto);
        RuleName saved = ruleNameRepository.save(entity);
        return ruleNameMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RuleNameDto getDto(Integer id) {
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RuleName introuvable avec l'id " + id));
        return ruleNameMapper.toDto(entity);
    }

    @Override
    public RuleNameDto update(Integer id, RuleNameDto dto) {
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RuleName introuvable avec l'id " + id));
        ruleNameMapper.updateEntity(entity, dto);
        RuleName saved = ruleNameRepository.save(entity);
        return ruleNameMapper.toDto(saved);
    }

    @Override
    public void delete(Integer id) {
        RuleName entity = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RuleName introuvable avec l'id " + id));
        ruleNameRepository.delete(entity);
    }
}