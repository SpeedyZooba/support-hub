package com.projects.supporthub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projects.supporthub.model.User;

public interface UserRepository extends JpaRepository<User, String>
{
    /**
     * Retrives the {@link User} with the email specified.
     * @param email the email of the {@link User}
     * @return a container with the requested {@link User}
     */
    public Optional<User> findByEmail(String email);
}