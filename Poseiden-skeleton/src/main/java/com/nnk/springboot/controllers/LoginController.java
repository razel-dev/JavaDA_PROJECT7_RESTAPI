package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping("/app/login")
    public String login() {
        return "login";
    }


    @GetMapping("error")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }


    @GetMapping("/403")
    public ModelAndView accessDenied() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMsg", "You are not authorized for the requested data.");
        mav.setViewName("403");
        return mav;
    }
}
