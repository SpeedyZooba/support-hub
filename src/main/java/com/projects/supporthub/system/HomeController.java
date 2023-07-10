package com.projects.supporthub.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController
{
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/login")
    public String loginPage()
    {
        log.info("Login page initiated.");
        return "login";
    }

    @GetMapping("/home")
    public String homepage()
    {
        return "home";
    }
}