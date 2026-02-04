package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.mapper.BidListMapper;
import com.nnk.springboot.repositories.BidListRepository;
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
class BidListServiceImplTest {

    @Mock
    BidListRepository bidListRepository;

    @Mock
    BidListMapper bidListMapper;

    @InjectMocks
    BidListServiceImpl service;


    private Integer id;
    private BidList entity;
    private BidListDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new BidList();
        entity.setId(id);
        entity.setAccount("acc");
        entity.setType("type");
        entity.setBidQuantity(new BigDecimal("10.00"));

        dto = new BidListDto();
        dto.setId(id);
        dto.setAccount("acc");
        dto.setType("type");
        dto.setBidQuantity(new BigDecimal("10.00"));
    }

    @Test
    void testFindAll() {
        // Arrange
        when(bidListRepository.findAll()).thenReturn(List.of(entity));
        when(bidListMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void testCreate() {
        // Arrange
        BidListDto input = new BidListDto();
        input.setAccount("acc");
        input.setType("type");
        input.setBidQuantity(new BigDecimal("10.00"));

        BidList toSave = new BidList();
        toSave.setAccount("acc");
        toSave.setType("type");
        toSave.setBidQuantity(new BigDecimal("10.00"));

        when(bidListMapper.toEntity(input)).thenReturn(toSave);
        when(bidListRepository.save(toSave)).thenReturn(entity);
        when(bidListMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getBidList() {
        // Arrange
        when(bidListRepository.findById(id)).thenReturn(Optional.of(entity));
        when(bidListMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getBidList(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("acc", result.getAccount());
    }

    @Test
    void delete() {
        // Arrange
        when(bidListRepository.existsById(id)).thenReturn(true);

        // Act
        service.delete(id);

        // Assert
        verify(bidListRepository).deleteById(id);
    }
}