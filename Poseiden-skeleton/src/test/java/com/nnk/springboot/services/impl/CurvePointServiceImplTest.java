package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceImplTest {

    @Mock
    CurvePointRepository curvePointRepository;

    @Mock
    CurvePointMapper curvePointMapper;

    @InjectMocks
    CurvePointServiceImpl service;

    private Integer id;
    private CurvePoint entity;
    private CurvePointDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new CurvePoint();
        entity.setId(id);
        entity.setCurveId(100);
        entity.setTerm(2.5);
        entity.setValue(10.0);
        entity.setAsOfDate(LocalDateTime.now().minusDays(1));
        entity.setCreationDate(LocalDateTime.now().minusDays(2));

        dto = new CurvePointDto();
        dto.setId(id);
        dto.setCurveId(100);
        dto.setTerm(2.5);
        dto.setValue(10.0);
        dto.setAsOfDate(entity.getAsOfDate());
        dto.setCreationDate(entity.getCreationDate());
    }

    @Test
    void create() {
        // Arrange
        CurvePointDto input = new CurvePointDto();
        input.setCurveId(200);
        input.setTerm(5.0);
        input.setValue(20.0);

        CurvePoint toSave = new CurvePoint();
        toSave.setCurveId(200);
        toSave.setTerm(5.0);
        toSave.setValue(20.0);

        when(curvePointMapper.toEntity(input)).thenReturn(toSave);
        when(curvePointRepository.save(toSave)).thenReturn(entity);
        when(curvePointMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getCurvePoint() {
        // Arrange
        when(curvePointRepository.findById(id)).thenReturn(Optional.of(entity));
        when(curvePointMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getCurvePoint(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals(100, result.getCurveId());
    }

    @Test
    void update() {
        // Arrange
        CurvePointDto updateDto = new CurvePointDto();
        updateDto.setCurveId(300);
        updateDto.setTerm(7.0);
        updateDto.setValue(30.0);

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(entity));
        when(curvePointRepository.save(entity)).thenReturn(entity);
        when(curvePointMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.update(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(curvePointMapper).updateEntity(entity, updateDto);
        verify(curvePointRepository).save(entity);
    }

    @Test
    void delete() {
        // Arrange
        when(curvePointRepository.existsById(id)).thenReturn(true);

        // Act
        service.delete(id);

        // Assert
        verify(curvePointRepository).deleteById(id);
    }
}