package com.projects.supporthub.service;

import java.util.List;
import com.projects.supporthub.model.User;

public interface UserService 
{
    public void newUser(User user);
    public void deleteUserById(String id);
    public User getUserById(String id);
    public List<User> getAllUsers();
}