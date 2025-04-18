package com.projects.supporthub.security;

import java.util.UUID;

import com.projects.supporthub.model.User;

public interface SecurityService 
{
    public boolean isAdmin(User user);
    public boolean isValidPassword(String password);
    public void firstLoginHandler(User user);
    public boolean userIdVerification(String username);
    public boolean ticketIdVerification(UUID id);
    public User sessionOwnerRetrieval();
    public void forceLogout(String username);
}