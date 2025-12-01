package com.buildenough.logisticsmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // /stocks.html 으로 리다이렉트
        return "redirect:/stocks.html";
    }
}