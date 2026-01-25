package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User create(UserDto dto);
    User update(Integer id, UserDto dto);
    void delete(Integer id);
    UserDto getDto(Integer id);
}