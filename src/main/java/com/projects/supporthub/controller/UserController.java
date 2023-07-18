package com.projects.supporthub.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.UserService;

@Controller
@EnableMethodSecurity
@RequestMapping("/profile")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController 
{
    private SecurityService verifier;
    private UserService users;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final PasswordEncoder encryptor = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(SecurityService verifier, UserService users)
    {
        this.verifier = verifier;
        this.users = users;
    }

    @GetMapping
    public ModelAndView displayUserDetails()
    {
        log.info("displayUserDetails has begun execution.");
        log.info("Retrieving the User of this session.");
        User currentUser = verifier.sessionOwnerRetrieval();
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", currentUser);
        log.info("displayUserDetails is about to finish execution.");
        return mav;
    }

    @GetMapping("/edit")
    public String initUpdateForm(Model model)
    {
        log.info("initUpdateForm has begun execution.");
        log.info("Retrieving the User of this session.");
        User currentUser = verifier.sessionOwnerRetrieval();
        model.addAttribute("userToEdit", currentUser);
        log.info("initUpdateForm is about to finish execution.");
        return "profileform";
    }

    @PostMapping("/edit")
    public String processUpdateForm(@Valid @ModelAttribute("userToEdit") User user, BindingResult result)
    {
        log.info("processUpdateForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        users.newUser(user);
        log.info("processUpdateForm is about to finish execution.");
        return "redirect:/profile";
    }

    @GetMapping("/setpassword")
    public String initPasswordForm(Model model)
    {
        log.info("initPasswordForm has begun execution.");
        log.info("Retrieving the User of this session.");
        User currentUser = verifier.sessionOwnerRetrieval();
        model.addAttribute("firstUser", currentUser);
        log.info("initPasswordForm is about to finish execution.");
        return "passwordform";
    }

    @PostMapping("/setpassword")
    public String processPasswordForm(@Valid @ModelAttribute("firstUser") User user, BindingResult result)
    {
        log.info("processPasswordForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        user.setPassword(encryptor.encode(user.getPassword()));
        users.newUser(user);
        log.info("processPasswordForm is about to finish execution.");
        return "redirect:/profile";
    }
}