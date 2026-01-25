package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result) {

        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.create(userDto);
            return "redirect:/user/list";
        } catch (IllegalArgumentException ex) {
            result.rejectValue("username", "username.exists", ex.getMessage());
            return "user/add";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.getDto(id));
        return "user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "user/update";
        }
        try {
            userService.update(id, userDto);
            return "redirect:/user/list";
        } catch (IllegalArgumentException ex) {
            result.rejectValue("username", "username.exists", ex.getMessage());
            return "user/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/user/list";
    }
}
