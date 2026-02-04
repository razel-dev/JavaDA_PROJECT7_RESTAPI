package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.mapper.RuleNameMapper;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceImplTest {

    @Mock
    RuleNameRepository ruleNameRepository;

    @Mock
    RuleNameMapper ruleNameMapper;

    @InjectMocks
    RuleNameServiceImpl service;

    private Integer id;
    private RuleName entity;
    private RuleNameDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new RuleName();
        entity.setId(id);
        entity.setName("name");
        entity.setDescription("desc");

        dto = new RuleNameDto();
        dto.setId(id);
        dto.setName("name");
        dto.setDescription("desc");
    }

    @Test
    void findAll() {
        // Arrange
        when(ruleNameRepository.findAll()).thenReturn(List.of(entity));
        when(ruleNameMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void create() {
        // Arrange
        RuleNameDto input = new RuleNameDto();
        input.setName("n2");
        input.setDescription("d2");

        RuleName toSave = new RuleName();
        toSave.setName("n2");
        toSave.setDescription("d2");

        when(ruleNameMapper.toEntity(input)).thenReturn(toSave);
        when(ruleNameRepository.save(toSave)).thenReturn(entity);
        when(ruleNameMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getRuleName() {
        // Arrange
        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ruleNameMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getRuleName(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("name", result.getName());
    }

    @Test
    void update() {
        // Arrange
        RuleNameDto updateDto = new RuleNameDto();
        updateDto.setName("nameUpdated");
        updateDto.setDescription("descUpdated");

        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ruleNameRepository.save(entity)).thenReturn(entity);
        when(ruleNameMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.update(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(ruleNameMapper).updateEntity(entity, updateDto);
        verify(ruleNameRepository).save(entity);
    }

    @Test
    void delete() {
        // Arrange
        when(ruleNameRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        service.delete(id);

        // Assert
        verify(ruleNameRepository).delete(entity);
    }
}