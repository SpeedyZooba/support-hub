package com.projects.supporthub.service;

import java.util.List;

import com.projects.supporthub.model.Role;

public interface RoleService 
{
    public List<Role> getRole(String name);
}