package com.projects.supporthub.service.implementation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.UserNotFoundException;
import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.security.recovery.TokenRepository;
import com.projects.supporthub.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepo;
    private TokenRepository tokenRepo;
    private TicketRepository ticketRepo;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepo, TokenRepository tokenRepo, TicketRepository ticketRepo)
    {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.ticketRepo = ticketRepo;
    }

    public void newUser(User user)
    {
        log.debug("Inside service method newUser.");
        log.debug("Service method newUser calls repo method save.");
        userRepo.save(user);
    }

    public void deleteUserById(String id)
    {
        log.debug("Inside service method deleteUserById.");
        log.debug("Service method deleteUserById is about to call repo method findById.");
        if (!userRepo.findById(id).isPresent())
        {
            log.error("Received invalid userId.");
            throw new UserNotFoundException("The requested user was not found.");
        }
        else
        {
            log.debug("Service method deleteUserById calls repo methods deleteById and deleteByCreatorId.");
            // purging the tickets and recovery tokens issued by the user specified before deleting the user itself.
            tokenRepo.deleteByUserId(id);
            ticketRepo.deleteByCreatorId(id);
            userRepo.deleteById(id);
        }
    }

    public User getUserById(String id)
    {
        log.debug("Inside service method getUserById.");
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent())
        {
            log.debug("Received invalid userId.");
            throw new UserNotFoundException("The requested user was not found.");
        }
        else
        {
            log.debug("Returning User from the container.");
            return user.get();
        }
    }
    
    public User getUserByUsername(String username)
    {
        log.debug("Inside service method getUserByUsername");
        Optional<User> user = userRepo.findByEmail(username);
        if (!user.isPresent())
        {
            log.debug("Received invalid email.");
            throw new UserNotFoundException("The requested user was not found.");
        }
        else
        {
            log.debug("Returning user from the container.");
            return user.get();
        }
    }

    public Page<User> getAllUsers(Pageable pageable)
    {
        log.debug("Inside service method getAllUsers.");
        log.debug("Service method getAllUsers calls repo method findAll for return.");
        return userRepo.findAll(pageable);
    }
}