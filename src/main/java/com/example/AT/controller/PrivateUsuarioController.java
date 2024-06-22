package com.example.AT.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class PrivateUsuarioController {
    @GetMapping("/oi")
    public String oi() {
        return "Olá, prof!";
    }
}
