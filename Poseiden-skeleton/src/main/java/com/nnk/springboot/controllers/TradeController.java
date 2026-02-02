package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.services.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        log.info("GET /trade/list - affichage de la liste des trades");
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Model model) {
        log.info("GET /trade/add - formulaire d'ajout de trade");
        model.addAttribute("trade", new TradeDto());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid TradeDto trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la création de Trade: {}", result.getAllErrors());
            model.addAttribute("trade", trade);
            return "trade/add";
        }
        tradeService.create(trade);
        log.info("Création de Trade réussie -> redirection vers la liste");
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /trade/update/{} - formulaire de mise à jour", id);
        model.addAttribute("trade", tradeService.getTrade(id));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour Trade id={}: {}", id, result.getAllErrors());
            model.addAttribute("trade", trade);
            return "trade/update";
        }
        tradeService.update(id, trade);
        log.info("Mise à jour Trade réussie id={} -> redirection vers la liste", id);
        return "redirect:/trade/list";
    }

    @PostMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.delete(id);
        log.info("Suppression Trade réussie id={} -> redirection vers la liste", id);
        return "redirect:/trade/list";
    }
}
