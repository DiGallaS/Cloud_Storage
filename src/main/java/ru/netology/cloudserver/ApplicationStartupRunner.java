package ru.netology.cloudserver;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.netology.cloudserver.entity.Users;
import ru.netology.cloudserver.repository.UserRepository;

@AllArgsConstructor
@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        userRepository.save(Users.builder()
                .login("basa1@gmail.com")
                .password(passwordEncoder.encode("11111"))
                .build());

    }
}
