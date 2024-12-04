package com.example.webshopmenswear.controller;


import com.example.webshopmenswear.model.Enum.UserRole;
import com.example.webshopmenswear.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {
    private final AuthService authService;


    @GetMapping
    public String getUserPage(Model model) {
        model.addAttribute("users", authService.getAllUsers());
        return "/admin/user/index";
    }

    @GetMapping("/create")
    public String createUserPage(Model model) {
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        System.out.println("roles" + roles.toString());
        return "/admin/user/create";
    }

    @GetMapping("/detail/{id}")
    public String getUserDetailPage(@PathVariable Integer id, Model model) {
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        model.addAttribute("user", authService.getUserById(id));
        return "/admin/user/detail";
    }
}
