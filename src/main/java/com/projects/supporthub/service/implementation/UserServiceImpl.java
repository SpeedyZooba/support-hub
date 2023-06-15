package com.projects.supporthub.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepo;
    private TicketRepository ticketRepo;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepo, TicketRepository ticketRepo)
    {
        this.userRepo = userRepo;
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
        if (userRepo.findById(id).isEmpty())
        {
            log.error("Received invalid userId.");
            throw new EntityNotFoundException("The requested user was not found.");
        }
        else
        {
            log.debug("Service method deleteUserById calls repo methods deleteById and deleteByCreatorId.");
            // purging the tickets issued by the user specified before deleting the user itself.
            ticketRepo.deleteByCreatorId(id);
            userRepo.deleteById(id);
        }
    }

    public User getUserById(String id)
    {
        log.debug("Inside service method getUserById.");
        log.debug("Service method getUserById is about to call repo method findById.");
        if (userRepo.findById(id).isEmpty())
        {
            log.debug("Received invalid userId.");
            throw new EntityNotFoundException("The requested user not found.");
        }
        else
        {
            log.debug("Service method getUserById calls repo method findById for return.");
            return userRepo.findById(id).get();
        }
    }

    public Page<User> getAllUsers(Pageable pageable)
    {
        log.debug("Inside service method getAllUsers.");
        log.debug("Service method getAllUsers calls repo method findAll for return.");
        return userRepo.findAll(pageable);
    }
}