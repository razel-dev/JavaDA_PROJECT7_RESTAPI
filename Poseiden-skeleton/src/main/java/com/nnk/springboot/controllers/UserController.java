package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/list")
    public String list(Model model) {
        log.info("GET /user/list - affichage de la liste des utilisateurs");
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        log.info("GET /user/add - formulaire d'ajout d'utilisateur");
        model.addAttribute("user", new UserDto());
        return "user/add";
    }

    @PostMapping("/validate")
    public String validate(@Validated(UserDto.Create.class) @ModelAttribute("user") UserDto userDto,
                           BindingResult result) {

        if (result.hasErrors()) {
            log.warn("Validation échouée à la création d'utilisateur: {}", result.getAllErrors());
            return "user/add";
        }
        try {
            userService.create(userDto);
            log.info("Création d'utilisateur réussie -> redirection vers la liste");
            return "redirect:/user/list";
        } catch (IllegalArgumentException ex) {
            log.warn("Création d'utilisateur refusée: {}", ex.getMessage());
            result.rejectValue("username", "username.exists", ex.getMessage());
            return "user/add";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /user/update/{} - formulaire de mise à jour", id);
        model.addAttribute("user", userService.getUser(id));
        return "user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Validated(UserDto.Update.class) @ModelAttribute("user") UserDto userDto,
                             BindingResult result) {

        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour utilisateur id={}: {}", id, result.getAllErrors());
            return "user/update";
        }
        try {
            userService.update(id, userDto);
            log.info("Mise à jour utilisateur réussie id={} -> redirection vers la liste", id);
            return "redirect:/user/list";
        } catch (IllegalArgumentException ex) {
            log.warn("Mise à jour utilisateur refusée id={}: {}", id, ex.getMessage());
            result.rejectValue("username", "username.exists", ex.getMessage());
            return "user/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        log.info("Suppression utilisateur réussie id={} -> redirection vers la liste", id);
        return "redirect:/user/list";
    }
}