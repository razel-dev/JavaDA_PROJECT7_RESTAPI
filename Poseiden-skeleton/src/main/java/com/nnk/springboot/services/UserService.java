package com.nnk.springboot.services;

import com.nnk.springboot.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto create(UserDto dto);
    UserDto update(Integer id, UserDto dto);
    void delete(Integer id);
    UserDto getDto(Integer id);
}