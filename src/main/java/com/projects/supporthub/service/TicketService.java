package com.projects.supporthub.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projects.supporthub.model.Ticket;

public interface TicketService 
{
    public void newTicket(Ticket ticket);
    public Ticket getTicketById(UUID id);
    public Page<Ticket> getTicketByUserId(String id, Pageable pageable);
    public Page<Ticket> getAllTickets(Pageable pageable);
    public List<Ticket> getLatestTickets(String userId);
    public List<Ticket> getAllLatestTickets();
}