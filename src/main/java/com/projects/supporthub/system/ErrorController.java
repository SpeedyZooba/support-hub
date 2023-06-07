package com.projects.supporthub.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController 
{
    @GetMapping("/error")
    public String systemError()
    {
        return "error";
    }
}
