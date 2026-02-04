package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.mapper.TradeMapper;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {

    @Mock
    TradeRepository tradeRepository;

    @Mock
    TradeMapper tradeMapper;

    @InjectMocks
    TradeServiceImpl service;

    private Integer id;
    private Trade entity;
    private TradeDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new Trade();
        entity.setId(id);
        entity.setAccount("acc");
        entity.setType("type");
        entity.setBuyQuantity(new BigDecimal("10.00"));

        dto = new TradeDto();
        dto.setId(id);
        dto.setAccount("acc");
        dto.setType("type");
        dto.setBuyQuantity(new BigDecimal("10.00"));
    }


    @Test
    void findAll() {
        // Arrange
        when(tradeRepository.findAll()).thenReturn(List.of(entity));
        when(tradeMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void create() {
        // Arrange
        TradeDto input = new TradeDto();
        input.setAccount("acc2");
        input.setType("type2");
        input.setBuyQuantity(new BigDecimal("20.00"));

        Trade toSave = new Trade();
        toSave.setAccount("acc2");
        toSave.setType("type2");
        toSave.setBuyQuantity(new BigDecimal("20.00"));

        when(tradeMapper.toEntity(input)).thenReturn(toSave);
        when(tradeRepository.save(toSave)).thenReturn(entity);
        when(tradeMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getTrade() {
        // Arrange
        when(tradeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tradeMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getTrade(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("acc", result.getAccount());
    }

    @Test
    void update() {
        // Arrange
        TradeDto updateDto = new TradeDto();
        updateDto.setAccount("accUpdated");
        updateDto.setType("typeUpdated");
        updateDto.setBuyQuantity(new BigDecimal("30.00"));

        when(tradeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tradeRepository.save(entity)).thenReturn(entity);
        when(tradeMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.update(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(tradeMapper).updateEntity(entity, updateDto);
        verify(tradeRepository).save(entity);
    }

    @Test
    void delete() {
        // Arrange
        when(tradeRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        service.delete(id);

        // Assert
        verify(tradeRepository).delete(entity);
    }
}