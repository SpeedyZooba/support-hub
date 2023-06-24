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
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User implements Serializable
{
    @Id
    @Column(name = "id_number", updatable = false, nullable = false)
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
    private LocalDate dateOfBirth;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "title", nullable = false)
    private String title;

    @Transient
    private boolean firstLogin = true;

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
    
    public Set<Role> getRoles() 
    {
        return roles;
    }

    public boolean getFirstLogin()
    {
        return firstLogin;
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
        if (isModifiable())
        {
            this.firstLogin = firstLogin;
        }
    }

    public void setRoles(Set<Role> roles) 
    {
        this.roles = roles;
    }

    private boolean isModifiable()
    {
        if (firstLogin == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}