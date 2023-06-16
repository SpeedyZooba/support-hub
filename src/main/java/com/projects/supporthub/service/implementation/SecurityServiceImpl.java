package com.projects.supporthub.service.implementation;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    public SecurityServiceImpl(UserService users, TicketService tickets, UserDetailsService details, AuthenticationManager authManager)
    {
        this.users = users;
        this.tickets = tickets;
        this.details = details;
        this.authManager = authManager;
    }

    public boolean login(String username, String password)
    {
        log.debug("Inside service method login.");
        log.debug("Service method login calls service method loadByUsername.");
        UserDetails userToLoad = details.loadUserByUsername(username);
        log.debug("Starting token creation.");
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(username, password, userToLoad.getAuthorities());
        boolean response = authManager.authenticate(loginToken).isAuthenticated();
        if (response)
        {
            log.debug("Successful authentication.");
            SecurityContextHolder.getContext().setAuthentication(loginToken);
        }
        log.debug("Returning authentication result and terminating.");
        return response;
    }

    public boolean userIdVerification(String id)
    {
        log.debug("Inside service method userIdVerification.");
        log.debug("Populating user details from the principal.");
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = detailsToLoad.getUsername();
        log.debug("Detail population success.");
        log.debug("Returning user id check result.");
        return users.getUserByUsername(username).getUserId().equals(id);
    }

    public boolean ticketIdVerification(UUID id)
    {
        log.debug("Inside service method ticketIdVerification");
        log.debug("Populating user details from the principal.");
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = detailsToLoad.getUsername();
        log.debug("Detail population success.");
        log.debug("Returning user id check result.");
        return tickets.getTicketById(id).getCreatedBy().equals(users.getUserByUsername(username).getUserId());
    }
}