package com.projects.supporthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.supporthub.model.Role;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Integer> 
{
    public List<Role> findByName(String name);
}