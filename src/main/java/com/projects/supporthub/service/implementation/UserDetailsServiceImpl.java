package com.projects.supporthub.service.implementation;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private UserRepository userRepo;

    public UserDetailsServiceImpl(UserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    public UserDetails loadUserByUsername(String username)
    {
        // retrieval of the user from the database based on username, which is the email of the user in this case
        Optional<User> userToLoad = userRepo.findByEmail(username);
        if (!userToLoad.isPresent())
        {
            throw new EntityNotFoundException("No such user exists.");
        }
        else
        {
            User user = userToLoad.get();
            UserDetails details = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
            return details;
        }
    }
}