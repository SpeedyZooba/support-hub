package com.projects.supporthub.controller;

import javax.validation.Valid;

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
        ModelAndView mav = new ModelAndView("/profile");
        User userFound = users.getUserById(userId);
        mav.addObject("user", userFound);
        return mav;
    }

    @GetMapping("/{userId}/edit")
    public String initUpdateForm(@PathVariable("userId") String userId, Model model)
    {
        User userFound = users.getUserById(userId);
        model.addAttribute("userToEdit", userFound);
        return "/updateUserForm";
    }

    @PostMapping("/{userId}/edit")
    public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") String userId)
    {
        if (result.hasErrors())
        {
            return ERROR_REDIRECTION;
        }
        users.newUser(user);
        return "/{userId}";
    }
}