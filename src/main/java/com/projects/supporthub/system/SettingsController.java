package com.projects.supporthub.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("darkMode")
public class SettingsController 
{
    @GetMapping("/settings")
    public String settings(Model model)
    {
        if (!model.containsAttribute("darkMode"))
        {
            model.addAttribute("darkMode", false);
        }
        return "settings";
    }

    @PostMapping("/settings/toggledark")
    public String toggleDarkMode(@ModelAttribute("darkMode") boolean darkMode)
    {
        darkMode = !darkMode;
        return "redirect:/settings";
    }
}
