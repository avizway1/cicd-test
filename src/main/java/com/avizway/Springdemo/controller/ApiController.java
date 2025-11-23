package com.avizway.Springdemo.controller;

import com.avizway.Springdemo.model.User;
import com.avizway.Springdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "Application is running");
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }

    @GetMapping("/user/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        userInfo.put("authenticated", authentication.isAuthenticated());
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // Remove passwords from response
        users.forEach(user -> user.setPassword("***"));
        return ResponseEntity.ok(users);
    }
}
