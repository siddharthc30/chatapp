package com.chatapp.controller;

import com.chatapp.entity.User;
import com.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping("/{username}/language")
    public void updateUserLanguage(
            @PathVariable String username,
            @RequestBody String language) {
        userService.updateUserLanguage(username, language);
    }
}