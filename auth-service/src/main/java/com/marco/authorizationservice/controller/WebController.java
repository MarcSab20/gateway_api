package com.marco.authorizationservice.controller;

import com.marco.authorizationservice.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/")
    public String homepage(Model model) {
        model.addAttribute("services", authorizationService.getAllServices());
        return "index";
    }

    @PostMapping("/toggle/{serviceId}")
    public String toggleService(@PathVariable String serviceId) {
        authorizationService.toggleServiceAuthorization(serviceId);
        return "redirect:/";
    }
}