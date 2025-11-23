package com.avizway.Springdemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        return "dashboard";
    }
}
