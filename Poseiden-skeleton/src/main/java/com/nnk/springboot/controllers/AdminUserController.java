package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;


    @GetMapping
    public List<UserDto> listAll() {
        return userService.findAll();
    }

    @PatchMapping("/{id}/role")
    public UserDto updateRole(@PathVariable Integer id, @RequestParam String role) {
        return userService.changeUserRole(id, role);
    }
}