package com.projects.supporthub.service;

import java.util.UUID;

import com.projects.supporthub.model.User;

public interface SecurityService 
{
    public boolean userIdVerification(String username);
    public boolean ticketIdVerification(UUID id);
    public void firstLoginVerification(User user);
    public User sessionOwnerRetrieval();
}