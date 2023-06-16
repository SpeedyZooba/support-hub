package com.projects.supporthub.service.implementation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    public UserDetails loadUserByUsername(String username)
    {
        log.debug("Inside service method loadByUsername.");
        log.debug("Service method loadByUsername calls repo method findByEmail.");
        // retrieval of the user from the database based on username, which is the email of the user in this case
        Optional<User> userToLoad = userRepo.findByEmail(username);
        if (!userToLoad.isPresent())
        {
            log.debug("Received invalid email.");
            throw new EntityNotFoundException("No such user exists.");
        }
        else
        {
            User user = userToLoad.get();
            log.debug("Populating principal with the details of the user loaded.");
            UserDetails details = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
            log.debug("Returning details and terminating.");
            return details;
        }
    }
}