package com.mlkb.ftm.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {
    @GetMapping("/")
    public String greetings() {
        return "Wejście Smoka. Test CI/CD. Test Pipeline";
    }
}
