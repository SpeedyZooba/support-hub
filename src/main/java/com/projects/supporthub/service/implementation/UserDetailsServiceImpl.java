package com.projects.supporthub.service.implementation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.BadCredentialException;
import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository userRepo;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    public UserDetails loadUserByUsername(String username)
    {
        log.info("Inside service method loadByUsername.");
        log.info("Service method loadByUsername calls repo method findByEmail.");
        // retrieval of the user from the database based on username, which is the email of the user in this case
        Optional<User> userToLoad = userRepo.findByEmail(username);
        if (!userToLoad.isPresent())
        {
            log.info("Received invalid email.");
            throw new BadCredentialException("No such user exists.");
        }
        else
        {
            User user = userToLoad.get();
            log.info("Populating principal with the details of the user loaded.");
            UserDetails details = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
            log.info("Returning details and terminating.");
            return details;
        }
    }
}