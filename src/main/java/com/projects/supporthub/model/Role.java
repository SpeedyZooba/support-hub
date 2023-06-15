package com.projects.supporthub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    public int getId() 
    {
        return id;
    }

    public String getRoleName() 
    {
        return roleName;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }
}