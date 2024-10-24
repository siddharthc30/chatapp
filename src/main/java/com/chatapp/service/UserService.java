package com.chatapp.service;

import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findById(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPreferredLanguage("en"); // Default to English
                    return userRepository.save(newUser);
                });
    }

    public void updateUserLanguage(String username, String language) {
        User user = getUser(username);
        user.setPreferredLanguage(language);
        userRepository.save(user);
    }
}