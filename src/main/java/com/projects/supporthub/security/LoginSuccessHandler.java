package com.projects.supporthub.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.projects.supporthub.model.User;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    private UserService users;
    @Autowired
    private SecurityService security;

    private static final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);

    private RedirectStrategy strat = new DefaultRedirectStrategy();

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        log.info("Principal retrieval for checking.");
        UserDetails details = (UserDetails) authentication.getPrincipal();
        log.info("Username retrieval for checking.");
        String username = details.getUsername();
        log.info("Populating the field with the User of the current session.");
        User userToCheck = users.getUserByUsername(username);
        log.info("Loading targetUrl using helper method.");
        String targetUrl = determineTargetUrl(userToCheck);
        security.firstLoginVerification(userToCheck);
        strat.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(User user) 
    {
        log.info("Inside helper method determineTargetUrl.");
        if (user.hasRole())
        {
            if (user.getFirstLogin() == true)
            {
                return "/profile/setpassword";
            }
            else
            {
                return "/home";
            }
        }
        else
        {
            return "/login?denied";
        }
    }
}