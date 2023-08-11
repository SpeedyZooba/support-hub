package com.projects.supporthub.security;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.Role;
import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;

@Service("verifier")
public class SecurityServiceImpl implements SecurityService
{
    private final UserRepository userRepo;
    private final TicketRepository ticketRepo;

    @Autowired
    private SessionRegistry userRegistry;

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[+.$])(?!.*\\s).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    private static final Logger log = LoggerFactory.getLogger(SecurityServiceImpl.class);
    
    public SecurityServiceImpl(UserRepository userRepo, TicketRepository ticketRepo)
    {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
    }

    public boolean isAdmin(User user)
    {
        List<Role> roles = user.getRoles();
        for (Role role : roles)
        {
            if (role.getName().equals("ROLE_ADMIN"))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isValidPassword(String charSequence)
    {
        Matcher passMatcher = pattern.matcher(charSequence);
        return passMatcher.matches();
    }

    public void firstLoginHandler(User user)
    {
        log.info("Inside service method firstLoginVerification.");
        if (user.getFirstLogin() == true)
        {
            log.info("First-time user detected.");
            user.setFirstLogin(false);
            userRepo.save(user);
        }
    }

    /**
     * Checks whether the {@link User} ID given belongs to that of the session holder, and returns true if so.
     */
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

    /**
     * Checks whether the {@link Ticket} given belongs to the session holder, and returns true if so.
     */
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

    /**
     * Forces the user to logout in case of reauthentication
     */
    public void forceLogout(String username)
    {
        List<Object> principals = userRegistry.getAllPrincipals();
        for (Object principal : principals)
        {
            if (principal instanceof UserDetails)
            {
                UserDetails user = (UserDetails) principal;
                if (user.getUsername().equals(username))
                {
                    List<SessionInformation> sessions = userRegistry.getAllSessions(user, false);
                    for (SessionInformation session : sessions)
                    {
                        session.expireNow();
                    }
                }
            }
        }
    }
}