package com.nnk.springboot.services;

import com.nnk.springboot.dto.RuleNameDto;
import java.util.List;

public interface RuleNameService {
    List<RuleNameDto> findAll();
    RuleNameDto create(RuleNameDto dto);
    RuleNameDto update(Integer id, RuleNameDto dto);
    void delete(Integer id);
    RuleNameDto getDto(Integer id);
}