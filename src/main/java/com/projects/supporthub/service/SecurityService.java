package com.projects.supporthub.service;

import java.util.UUID;

public interface SecurityService 
{
    public boolean login(String username, String password);
    public boolean userIdVerification(String username);
    public boolean ticketIdVerification(UUID id);
}