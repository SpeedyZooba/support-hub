package com.projects.supporthub.service.implementation;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.service.SecurityService;

@Service
public class SecurityServiceImpl implements SecurityService
{
    private final UserRepository userRepo;
    private final TicketRepository ticketRepo;

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    public SecurityServiceImpl(UserRepository userRepo, TicketRepository ticketRepo, UserDetailsService details, AuthenticationManager authManager)
    {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
    }

    public boolean userIdVerification(String id)
    {
        log.info("Inside service method userIdVerification.");
        log.info("Populating user details from the principal.");
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = detailsToLoad.getUsername();
        log.info("Detail population success.");
        log.info("Returning user id check result.");
        return userRepo.findByEmail(username).get().getUserId().equals(id);
    }

    public boolean ticketIdVerification(UUID id)
    {
        log.info("Inside service method ticketIdVerification.");
        log.info("Populating UserDetails.");
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Detail population success.");
        String username = detailsToLoad.getUsername();
        log.info("Returning user id check result.");
        return ticketRepo.findById(id).get().getCreatedBy().equals(userRepo.findByEmail(username).get().getUserId());
    }

    public void firstLoginVerification(User user)
    {
        log.info("Inside service method firstLoginVerification.");
        if (user.getFirstLogin() == true)
        {
            user.setFirstLogin(false);
        }
    }

    /**
     * Retrieval of the {@link User} of the session for controllers to use.
     * @return {@link User} of this session
     */
    public User sessionOwnerRetrieval()
    {
        log.info("Inside service method sessionRetrieval.");
        log.info("Populating UserDetails.");
        UserDetails detailsToLoad = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Detail population success.");
        String username = detailsToLoad.getUsername();
        log.info("Returning User.");
        return userRepo.findByEmail(username).get();
    }
}