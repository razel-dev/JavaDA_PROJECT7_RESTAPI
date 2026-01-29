package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping("/app/register")
    public String register(
            @Validated(UserDto.Create.class) @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            @RequestParam String confirmPassword
    ) {

        if (result.hasErrors()) {
            return "redirect:/app/login?error";
        }


        if (userDto.getPassword() == null || !userDto.getPassword().equals(confirmPassword)) {
            return "redirect:/app/login?error";
        }


        userDto.setRole("USER");

        try {
            userService.create(userDto);
        } catch (IllegalArgumentException ex) {
            return "redirect:/app/login?error";
        }

        return "redirect:/app/login?registered";
    }
}