package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.repositories.RatingRepository;
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
class RatingServiceImplTest {

    @Mock
    RatingRepository ratingRepository;

    @Mock
    RatingMapper ratingMapper;

    @InjectMocks
    RatingServiceImpl service;

    private Integer id;
    private Rating entity;
    private RatingDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new Rating();
        entity.setId(id);
        entity.setMoodysRating("MOODYS_A");
        entity.setSandpRating("SANDP_A");
        entity.setFitchRating("FITCH_A");
        entity.setOrderNumber(10);

        dto = new RatingDto();
        dto.setId(id);
        dto.setMoodysRating("MOODYS_A");
        dto.setSandpRating("SANDP_A");
        dto.setFitchRating("FITCH_A");
        dto.setOrderNumber(10);
    }

    @Test
    void findAll() {
        // Arrange
        when(ratingRepository.findAll()).thenReturn(List.of(entity));
        when(ratingMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void getRating() {
        // Arrange
        when(ratingRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ratingMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getRating(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("MOODYS_A", result.getMoodysRating());
    }

    @Test
    void create() {
        // Arrange
        RatingDto input = new RatingDto();
        input.setMoodysRating("MOODYS_B");
        input.setSandpRating("SANDP_B");
        input.setFitchRating("FITCH_B");
        input.setOrderNumber(20);

        Rating toSave = new Rating();
        toSave.setMoodysRating("MOODYS_B");
        toSave.setSandpRating("SANDP_B");
        toSave.setFitchRating("FITCH_B");
        toSave.setOrderNumber(20);

        when(ratingMapper.toEntity(input)).thenReturn(toSave);
        when(ratingRepository.save(toSave)).thenReturn(entity);
        when(ratingMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void update() {
        // Arrange
        RatingDto updateDto = new RatingDto();
        updateDto.setMoodysRating("MOODYS_C");
        updateDto.setSandpRating("SANDP_C");
        updateDto.setFitchRating("FITCH_C");
        updateDto.setOrderNumber(30);

        when(ratingRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ratingRepository.save(entity)).thenReturn(entity);
        when(ratingMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.update(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(ratingMapper).updateEntity(entity, updateDto);
        verify(ratingRepository).save(entity);
    }

    @Test
    void delete() {
        // Arrange
        when(ratingRepository.existsById(id)).thenReturn(true);

        // Act
        service.delete(id);

        // Assert
        verify(ratingRepository).deleteById(id);
    }
}

