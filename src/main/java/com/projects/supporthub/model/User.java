package com.projects.supporthub.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(name = "id_number", nullable = false)
    private String userId;

    /**
     * Substitute for username/nickname in this system.
     */
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pass", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "to_be_verified", nullable = false)
    private boolean firstLogin;

    @Column(name = "last_pass_change")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastPassChange;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public String getUserId() 
    {
        return userId;
    }

    public String getEmail() 
    {
        return email;
    }

    public String getPassword() 
    {
        return password;
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
    
    public List<Role> getRoles() 
    {
        return roles;
    }

    public boolean getFirstLogin()
    {
        return firstLogin;
    }

    public LocalDateTime getLastPassChange() 
    {
        return lastPassChange;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setPassword(String password) 
    {
        this.password = password;
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

    public void setFirstLogin(boolean firstLogin) 
    {
        this.firstLogin = firstLogin;
    }

    public void setLastPassChange(LocalDateTime lastPassChange) 
    {
        this.lastPassChange = lastPassChange;
    }

    public void setRoles(List<Role> roles) 
    {
        this.roles = roles;
    }

    public boolean hasRole()
    {
        for (Role role : roles)
        {
            if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_USER"))
            {
                return true;
            }
        }
        return false;
    }
}