package com.projects.supporthub.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.UserService;

@Controller
public class HomeController
{
    private final UserService users;
    private final SecurityService verifier;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    public HomeController(UserService users, SecurityService verifier)
    {
        this.users = users;
        this.verifier = verifier;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes)
    {
        log.info("Passing credentials for authentication.");
        boolean response = verifier.login(username, password);
        if (response)
        {
            User userToLogin = users.getUserByUsername(username);
            log.info("Authentication success.");
            if (userToLogin.getFirstLogin() == true)
            {
                userToLogin.setFirstLogin(false);
                return new StringBuilder().append("redirect:/").append(userToLogin.getUserId()).append("/setpassword").toString();
            }
            else
            {
                log.info("Successfully logged in.");
                return new StringBuilder().append("redirect:/").append(userToLogin.getUserId()).toString();
            }
        }
        else
        {
            log.info("Authentication fail.");
            redirectAttributes.addFlashAttribute("failureMessage", "Invalid username or password.");
            return "redirect:/login";
        }
    }
}