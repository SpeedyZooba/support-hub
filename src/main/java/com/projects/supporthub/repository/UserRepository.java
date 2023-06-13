package com.projects.supporthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projects.supporthub.model.User;

public interface UserRepository extends JpaRepository<User, String>
{
    
}