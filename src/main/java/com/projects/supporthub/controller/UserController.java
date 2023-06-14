package com.projects.supporthub.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.UserService;

@Controller
public class UserController 
{
    private final UserService users;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final String[] BLACKLIST = {"userId", "email", "passwordHash"};
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService users)
    {
        this.users = users;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder)
    {
        dataBinder.setDisallowedFields(BLACKLIST);
    }

    @GetMapping("/{userId}")
    public ModelAndView displayUserDetails(@PathVariable("userId") String userId)
    {
        log.info("displayUserDetails has begun execution.");
        ModelAndView mav = new ModelAndView("/profile");
        User userFound = users.getUserById(userId);
        mav.addObject("user", userFound);
        log.info("displayUserDetails is about to finish execution.");
        return mav;
    }

    @GetMapping("/{userId}/edit")
    public String initUpdateForm(@PathVariable("userId") String userId, Model model)
    {
        log.info("initUpdateForm has begun execution.");
        User userFound = users.getUserById(userId);
        model.addAttribute("userToEdit", userFound);
        log.info("initUpdateForm is about to finish execution.");
        return "/updateUserForm";
    }

    @PostMapping("/{userId}/edit")
    public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") String userId)
    {
        log.info("processUpdateForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        users.newUser(user);
        log.info("processUpdateForm is about to finish execution.");
        return "/{userId}";
    }
}