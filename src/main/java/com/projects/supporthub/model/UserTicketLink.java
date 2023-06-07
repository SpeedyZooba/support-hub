package com.projects.supporthub.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Class to be used as a link between the user and tickets issued by them.
 * Upon the creation of a new {@link Ticket}, a link is also created to ease read/write/update operations.
 */
@Entity
@Table(name = "links")
public class UserTicketLink implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "ticket_id")
    private UUID ticketId;

    public int getId() 
    {
        return id;
    }

    public String getUserId() 
    {
        return userId;
    }

    public UUID getTicketId() 
    {
        return ticketId;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public void setUserId(String userId) 
    {
        this.userId = userId;
    }
    
    public void setTicketId(UUID ticketId) 
    {
        this.ticketId = ticketId;
    }
}
