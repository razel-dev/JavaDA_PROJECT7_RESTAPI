package com.nnk.springboot.services.impl;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.mapper.UserMapper;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl service;

    private Integer id;
    private User entity;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        id = 1;

        entity = new User();
        entity.setId(id);
        entity.setUsername("john");
        entity.setFullname("John Doe");
        entity.setRole("USER");
        entity.setPassword("hashed");

        dto = new UserDto();
        dto.setId(id);
        dto.setUsername("john");
        dto.setFullname("John Doe");
        dto.setRole("USER");
        dto.setPassword("hashed");
    }

    @Test
    void findAll() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void create() {
        // Arrange
        UserDto input = new UserDto();
        input.setUsername("alice");
        input.setFullname("Alice A");
        input.setRole("ADMIN");
        input.setPassword("secret");
        when(passwordEncoder.encode("secret")).thenReturn("ENC(secret)");
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(passwordEncoder).encode("secret");
        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(userMapper).toEntity(captor.capture());
        assertEquals("ENC(secret)", captor.getValue().getPassword());
        assertNull(captor.getValue().getId());
    }

    @Test
    void update() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setUsername("johnny");
        updateDto.setFullname("John Doe Updated");
        updateDto.setRole("ADMIN");
        updateDto.setPassword("newSecret");
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(passwordEncoder.encode("newSecret")).thenReturn("ENC(newSecret)");
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.update(id, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(passwordEncoder).encode("newSecret");
        verify(userMapper).updateEntity(eq(entity), any(UserDto.class));
        verify(userRepository).save(entity);
    }

    @Test
    void delete() {
        // Arrange
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        service.delete(id);

        // Assert
        verify(userRepository).delete(entity);
    }

    @Test
    void getUser() {
        // Arrange
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.getUser(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("john", result.getUsername());
    }

    @Test
    void register() {
        // Arrange
        when(passwordEncoder.encode("pwd")).thenReturn("ENC(pwd)");
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.register("bob", "Bob B", "pwd");

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(passwordEncoder).encode("pwd");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changeUserRole() {
        // Arrange
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);

        // Act
        var result = service.changeUserRole(id, "MANAGER");

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("MANAGER", entity.getRole());
        verify(userRepository).save(entity);
    }
}