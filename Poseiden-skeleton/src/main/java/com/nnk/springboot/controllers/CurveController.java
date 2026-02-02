package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CurveController {
    private final CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        log.info("GET /curvePoint/list - affichage de la liste des points de courbe");
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        log.info("GET /curvePoint/add - formulaire d'ajout");
        model.addAttribute("curvePoint", new CurvePointDto());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePointDto curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la création de CurvePoint: {}", result.getAllErrors());
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/add";
        }
        curvePointService.create(curvePoint);
        log.info("Création de CurvePoint réussie -> redirection vers la liste");
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /curvePoint/update/{} - formulaire de mise à jour", id);
        model.addAttribute("curvePoint", curvePointService.getCurvePoint(id));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid CurvePointDto curvePoint,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour CurvePoint id={}: {}", id, result.getAllErrors());
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        }
        curvePointService.update(id, curvePoint);
        log.info("Mise à jour CurvePoint réussie id={} -> redirection vers la liste", id);
        return "redirect:/curvePoint/list";
    }

    @PostMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id) {
        curvePointService.delete(id);
        log.info("Suppression CurvePoint réussie id={} -> redirection vers la liste", id);
        return "redirect:/curvePoint/list";
    }
}
