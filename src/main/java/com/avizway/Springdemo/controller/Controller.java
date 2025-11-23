package com.avizway.Springdemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
	
	@Value("${welcome.message}")
    private String message;
	
    @GetMapping("/")
    public String main(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && 
                                 !auth.getPrincipal().equals("anonymousUser");
        
        model.addAttribute("message", message);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            model.addAttribute("username", auth.getName());
        }
        return "welcome";
    }
}
