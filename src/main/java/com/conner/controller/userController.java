package com.conner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor

public class userController {
    @GetMapping("hello")
    public String getMethodName() {
        return "Hello world";
    }

}
