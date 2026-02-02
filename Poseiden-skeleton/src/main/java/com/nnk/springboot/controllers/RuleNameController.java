package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RuleNameController {

    private final RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        log.info("GET /ruleName/list - affichage de la liste des règles");
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        log.info("GET /ruleName/add - formulaire d'ajout de règle");
        model.addAttribute("ruleName", new RuleNameDto());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la création de RuleName: {}", result.getAllErrors());
            model.addAttribute("ruleName", ruleName);
            return "ruleName/add";
        }
        ruleNameService.create(ruleName);
        log.info("Création de RuleName réussie -> redirection vers la liste");
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /ruleName/update/{} - formulaire de mise à jour", id);
        model.addAttribute("ruleName", ruleNameService.getRuleName(id));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour RuleName id={}: {}", id, result.getAllErrors());
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }
        ruleNameService.update(id, ruleName);
        log.info("Mise à jour RuleName réussie id={} -> redirection vers la liste", id);
        return "redirect:/ruleName/list";
    }

    @PostMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.delete(id);
        log.info("Suppression RuleName réussie id={} -> redirection vers la liste", id);
        return "redirect:/ruleName/list";
    }
}
