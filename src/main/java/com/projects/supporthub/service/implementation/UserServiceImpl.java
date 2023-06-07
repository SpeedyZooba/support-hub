package com.projects.supporthub.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.repository.UserTicketLinkRepository;
import com.projects.supporthub.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepo;
    private TicketRepository ticketRepo;
    private UserTicketLinkRepository linkRepo;

    public UserServiceImpl(UserRepository userRepo, TicketRepository ticketRepo, UserTicketLinkRepository linkRepo)
    {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
        this.linkRepo = linkRepo;
    }

    public void newUser(User user)
    {
        userRepo.save(user);
    }

    public void deleteUserById(String id)
    {
        userRepo.deleteById(id);
        linkRepo.deleteByUserId(id);
        ticketRepo.deleteByIssuerId(id);
    }

    public User getUserById(String id)
    {
        return userRepo.findById(id).get();
    }

    public List<User> getAllUsers()
    {
        return userRepo.findAll();
    }
}
