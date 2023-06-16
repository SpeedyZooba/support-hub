package com.projects.supporthub.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.UserService;

@Controller
public class HomeController
{
    private final UserService users;
    private final SecurityService verifier;

    public HomeController(UserService users, SecurityService verifier)
    {
        this.users = users;
        this.verifier = verifier;
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model)
    {
        boolean response = verifier.login(email, password);
        if (response)
        {
            model.addAttribute("successMessage", "Successfully logged in.");
            return new StringBuilder().append("/").append(users.getUserByUsername(password).getUserId()).toString();
        }
        else
        {
            model.addAttribute("failureMessage", "Invalid email or password.");
            return "/login";
        }
    }
}