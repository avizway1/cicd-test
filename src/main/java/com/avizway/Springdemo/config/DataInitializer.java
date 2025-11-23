package com.avizway.Springdemo.config;

import com.avizway.Springdemo.model.User;
import com.avizway.Springdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@avizway.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
            admin.setEnabled(true);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setEmail("user@avizway.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Demo User");
            user.setRoles(Set.of("ROLE_USER"));
            user.setEnabled(true);
            userRepository.save(user);

            log.info("Demo users created - admin:admin123 and user:user123");
        }
    }
}
