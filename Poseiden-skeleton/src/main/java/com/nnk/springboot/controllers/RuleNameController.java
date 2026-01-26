package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RuleNameController {

    private final RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleNameDto());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/add";
        }
        ruleNameService.create(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ruleName", ruleNameService.getDto(id));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleNameDto ruleName,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }
        ruleNameService.update(id, ruleName);
        return "redirect:/ruleName/list";
    }

    @PostMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.delete(id);
        return "redirect:/ruleName/list";
    }
}
