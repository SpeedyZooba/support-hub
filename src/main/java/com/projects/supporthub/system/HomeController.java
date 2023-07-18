package com.projects.supporthub.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@EnableMethodSecurity
public class HomeController
{
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    
    @GetMapping("/home")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String homepage()
    {
        log.info("Homepage initiated.");
        return "home";
    }
}