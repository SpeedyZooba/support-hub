package com.projects.supporthub.security.recovery;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tokens")
public class RecoveryToken implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "owned_by", nullable = false)
    private String userId;

    @Column(name = "token_string", nullable = false)
    private String tokenString;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    public int getId() 
    {
        return id;
    }

    public String getUserId() 
    {
        return userId;
    }

    public String getTokenString() 
    {
        return tokenString;
    }

    public LocalDateTime getExpiryDate() 
    {
        return expiryDate;
    }

    public boolean getIsUsed()
    {
        return isUsed;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public void setTokenString(String tokenString) 
    {
        this.tokenString = tokenString;
    }

    public void setExpiryDate(LocalDateTime expiryDate) 
    {
        this.expiryDate = expiryDate;
    }

    public void setIsUsed(boolean isUsed) 
    {
        this.isUsed = isUsed;
    }

    public boolean isUsed()
    {
        if (isUsed == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isExpired()
    {
        if (LocalDateTime.now().isAfter(expiryDate))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}