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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.UserService;

@Controller
@EnableMethodSecurity
@RequestMapping("/{userId}")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN') and #userId == @verifier.userIdVerification(#userId)")
public class UserController 
{
    private final SecurityService verifier;
    private final UserService users;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final String[] BLACKLIST = {"userId", "email", "passwordHash"};
    private static final PasswordEncoder encryptor = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(SecurityService verifier, UserService users)
    {
        this.verifier = verifier;
        this.users = users;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder)
    {
        dataBinder.setDisallowedFields(BLACKLIST);
    }

    @GetMapping("/profile")
    public ModelAndView displayUserDetails(@PathVariable("userId") String userId)
    {
        log.info("displayUserDetails has begun execution.");
        ModelAndView mav = new ModelAndView("details");
        User userFound = users.getUserById(userId);
        mav.addObject("user", userFound);
        log.info("displayUserDetails is about to finish execution.");
        return mav;
    }

    @GetMapping("/edit")
    public String initUpdateForm(@PathVariable("userId") String userId, Model model)
    {
        log.info("initUpdateForm has begun execution.");
        User userFound = users.getUserById(userId);
        model.addAttribute("userToEdit", userFound);
        log.info("initUpdateForm is about to finish execution.");
        return "updateuserform";
    }

    @PostMapping("/edit")
    public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") String userId)
    {
        log.info("processUpdateForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        user.setPassword(encryptor.encode(user.getPassword()));
        users.newUser(user);
        log.info("processUpdateForm is about to finish execution.");
        return "redirect:/{userId}/profile";
    }

    @GetMapping("/setpassword")
    public String initPasswordForm(@PathVariable("userId") String userId, Model model)
    {
        log.info("initPasswordForm has begun execution.");
        User userFound = users.getUserById(userId);
        model.addAttribute("firstUser", userFound);
        log.info("initPasswordForm is about to finish execution.");
        return "firstpasswordform";
    }

    @PostMapping("/setpassword")
    public String processPasswordForm(@Valid User user, BindingResult result, @RequestParam("password") String password, @PathVariable("userId") String userId)
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
        return "redirect:/{userId}";
    }
}