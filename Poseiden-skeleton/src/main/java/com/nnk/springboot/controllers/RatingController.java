package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;  // GetMapping, PostMapping, PathVariable, ModelAttribute

@Slf4j
@Controller
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @RequestMapping("/rating/list")
    public String home(Model model) {
        log.info("GET /rating/list - affichage de la liste des notations");
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }


    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        log.info("GET /rating/add - formulaire d'ajout de notation");
        model.addAttribute("rating", new RatingDto());
        return "rating/add";
    }


    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDto rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la création de Rating: {}", result.getAllErrors());
            model.addAttribute("rating", rating);
            return "rating/add";
        }
        ratingService.create(rating);
        log.info("Création de Rating réussie -> redirection vers la liste");
        return "redirect:/rating/list";
    }


    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /rating/update/{} - formulaire de mise à jour", id);
        model.addAttribute("rating", ratingService.getRating(id));
        return "rating/update";
    }


    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") RatingDto rating,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour Rating id={}: {}", id, result.getAllErrors());
            model.addAttribute("rating", rating);
            return "rating/update";
        }
        ratingService.update(id, rating);
        log.info("Mise à jour Rating réussie id={} -> redirection vers la liste", id);
        return "redirect:/rating/list";
    }


    @PostMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.delete(id);
        log.info("Suppression Rating réussie id={} -> redirection vers la liste", id);
        return "redirect:/rating/list";
    }
}
