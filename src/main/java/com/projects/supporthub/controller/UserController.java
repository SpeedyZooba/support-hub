package com.projects.supporthub.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.UserService;

@Controller
public class UserController 
{
    private final UserService users;

    public UserController(UserService users)
    {
        this.users = users;
    }

    @ModelAttribute("user")
    public User findUser(@PathVariable(name = "userId") String userId)
    {
        return users.getUserById(userId);
    }

    @InitBinder("user")
    public void setAllowedFields(WebDataBinder dataBinder)
    {
        dataBinder.setDisallowedFields("userId", "passwordHash");
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
    public ModelAndView initUpdateForm(@PathVariable("userId") String userId)
    {
        User userFound = users.getUserById(userId);
        return new ModelAndView("edit", "user", userFound);
    }

    @PostMapping("/{userId}/edit")
    public String processUpdateForm(@Valid User user, BindingResult result, @PathVariable("userId") String userId)
    {
        if (result.hasErrors())
        {
            return "redirect:/error";
        }
        user.setUserId(userId);
        users.newUser(user);
        return "/{userId}";
    }
}
