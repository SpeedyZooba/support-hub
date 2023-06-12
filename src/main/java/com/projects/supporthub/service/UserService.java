package com.projects.supporthub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projects.supporthub.model.User;

public interface UserService 
{
    public void newUser(User user);
    public void deleteUserById(String id);
    public User getUserById(String id);
    public Page<User> getAllUsers(Pageable pageable);
}