package com.example.webshopmenswear.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    @GetMapping
    public String getUserPage() {
        return "/admin/user/index";
    }

    @GetMapping("/create")
    public String createUserPage() {
        return "/admin/user/create";
    }

    @GetMapping("/detail/{id}")
    public String getUserDetailPage(@PathVariable Integer id) {
        return "/admin/user/detail";
    }
}
