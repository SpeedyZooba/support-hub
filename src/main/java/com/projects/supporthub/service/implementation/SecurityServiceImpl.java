package com.projects.supporthub.service.implementation;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.TicketService;
import com.projects.supporthub.service.UserService;

@Service
public class SecurityServiceImpl implements SecurityService
{
    private UserService users;
    private TicketService tickets;
    private UserDetailsService details;

    private AuthenticationManager authManager;
    
    public SecurityServiceImpl(UserService users, TicketService tickets, UserDetailsService details, AuthenticationManager authManager)
    {
        this.users = users;
        this.tickets = tickets;
        this.details = details;
        this.authManager = authManager;
    }

    public boolean login(String username, String password)
    {
        UserDetails userToLoad = details.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(username, password, userToLoad.getAuthorities());
        boolean response = authManager.authenticate(loginToken).isAuthenticated();
        if (response)
        {
            SecurityContextHolder.getContext().setAuthentication(loginToken);
        }
            return response;
    }

    public boolean userIdVerification(String id)
    {
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = detailsToLoad.getUsername();
        return users.getUserByUsername(username).getUserId().equals(id);
    }

    public boolean ticketIdVerification(UUID id)
    {
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = detailsToLoad.getUsername();
        return tickets.getTicketById(id).getCreatedBy().equals(users.getUserByUsername(username).getUserId());
    }
}