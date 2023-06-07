package com.projects.supporthub.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController 
{
    @GetMapping("/homepage")
    public String homepage()
    {
        return "home";
    }
}
