package com.projects.supporthub.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.UserNotFoundException;
import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
    private UserRepository userRepo;
    private TicketRepository ticketRepo;

    public UserServiceImpl(UserRepository userRepo, TicketRepository ticketRepo)
    {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
    }

    public void newUser(User user)
    {
        userRepo.save(user);
    }

    public void deleteUserById(String id)
    {
        if (userRepo.findById(id).isEmpty())
        {
            throw new UserNotFoundException("User not found.");
        }
        else
        {
            userRepo.deleteById(id);
            ticketRepo.deleteByCreatorId(id);
        }
    }

    public User getUserById(String id)
    {
        if (userRepo.findById(id).isEmpty())
        {
            throw new UserNotFoundException("User not found.");
        }
        return userRepo.findById(id).get();
    }

    public Page<User> getAllUsers(Pageable pageable)
    {
        return userRepo.findAll(pageable);
    }
}