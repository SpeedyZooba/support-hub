package com.projects.supporthub.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.Role;
import com.projects.supporthub.repository.RoleRepository;
import com.projects.supporthub.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService 
{
    private final RoleRepository roles;

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    public RoleServiceImpl(RoleRepository roles)
    {
        this.roles = roles;
    }

    public List<Role> getRole(String name)
    {
        log.info("Returning role from the service method getRole.");
        return roles.findByName(name);
    }
}