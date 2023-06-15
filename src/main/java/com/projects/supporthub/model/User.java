package com.projects.supporthub.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable
{
    @Id
    @Column(name = "id_number", updatable = false, nullable = false)
    private String userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pass_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getUserId() 
    {
        return userId;
    }

    public String getEmail() 
    {
        return email;
    }

    public String getPasswordHash() 
    {
        return passwordHash;
    }

    public String getFirstName() 
    {
        return firstName;
    }

    public String getLastName() 
    {
        return lastName;
    }

    public LocalDate getDateOfBirth() 
    {
        return dateOfBirth;
    }

    public String getDepartment() 
    {
        return department;
    }

    public String getTitle() 
    {
        return title;
    }
    
    public Set<Role> getRoles() 
    {
        return roles;
    }

    public void setRoles(Set<Role> roles) 
    {
        this.roles = roles;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) 
    {
        this.passwordHash = passwordHash;
    }
    
    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) 
    {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDepartment(String department) 
    {
        this.department = department;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }
}