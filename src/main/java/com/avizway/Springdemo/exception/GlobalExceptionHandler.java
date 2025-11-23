package com.avizway.Springdemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        log.error("Page not found: {}", ex.getRequestURL());
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The page you are looking for does not exist.");
        model.addAttribute("status", 404);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        log.error("Unexpected error occurred", ex);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred. Please try again later.");
        model.addAttribute("status", 500);
        return "error";
    }
}
