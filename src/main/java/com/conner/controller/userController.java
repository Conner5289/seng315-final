package com.conner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conner.model.User;
import com.conner.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class userController {
    private final UserService userService;

    @GetMapping("hello")
    public String getMethodName() {
        return "Hello world";
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {

        return userService.create(user);
    }
}
