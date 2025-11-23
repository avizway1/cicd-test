package com.avizway.Springdemo.controller;

import com.avizway.Springdemo.model.User;
import com.avizway.Springdemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            if (userService.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
                return "register";
            }

            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Registration error", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
