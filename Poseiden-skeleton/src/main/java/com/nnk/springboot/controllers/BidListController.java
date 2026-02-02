package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
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
public class BidListController {
    private final BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        log.info("GET /bidList/list - affichage de la liste des enchères");
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        log.info("GET /bidList/add - formulaire d'ajout");
        model.addAttribute("bidList", new BidListDto());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidListDto bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la création: {}", result.getAllErrors());
            model.addAttribute("bidList", bidList);
            return "bidList/add";
        }
        bidListService.create(bidList);
        log.info("Création réussie -> redirection vers la liste");
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("GET /bidList/update/{} - formulaire de mise à jour", id);
        model.addAttribute("bidList", bidListService.getBidList(id));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.warn("Validation échouée à la mise à jour id={}: {}", id, result.getAllErrors());
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }
        bidListService.update(id, bidList);
        log.info("Mise à jour réussie id={} -> redirection vers la liste", id);
        return "redirect:/bidList/list";
    }

    @PostMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.delete(id);
        log.info("Suppression réussie id={} -> redirection vers la liste", id);
        return "redirect:/bidList/list";
    }
}
