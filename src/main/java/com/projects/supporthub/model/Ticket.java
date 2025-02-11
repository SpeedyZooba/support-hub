package com.projects.supporthub.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket implements Serializable
{
    public enum Status {PENDING, ANSWERED}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_num", nullable = false, updatable = false)
    private UUID ticketId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "issue", nullable = false)
    private String description;

    @Column(name = "creator_id", nullable = false)
    private String createdBy;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ticket_status", columnDefinition = "stat" , nullable = false)
    @Type(PostgreSQLEnumType.class)
    private Status status;

    @Column(name = "response_date")
    private LocalDate answeredAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public UUID getTicketId() 
    {
        return ticketId;
    }

    public String getCategory() 
    {
        return category;
    }

    public String getDescription() 
    {
        return description;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }

    public LocalDate getCreatedAt() 
    {
        return createdAt;
    }

    public Status getStatus() 
    {
        return status;
    }
    
    public LocalDate getAnsweredAt() 
    {
        return answeredAt;
    }
    
    public boolean getIsDeleted()
    {
        return isDeleted;
    }

    public void setTicketId(UUID ticketId) 
    {
        this.ticketId = ticketId;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }
    
    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(LocalDate createdAt) 
    {
        this.createdAt = createdAt;
    }
    
    public void setAnsweredAt(LocalDate answeredAt) 
    {
        this.answeredAt = answeredAt;
    }

    public void setStatus(Status status) 
    {
        this.status = status;
    }

    public void setIsDeleted(boolean isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    @PrePersist
    public void initCreationDate()
    {
        createdAt = LocalDate.now();
    }

    @PreUpdate
    public void initUpdateDate()
    {
        answeredAt = LocalDate.now();
    }
}