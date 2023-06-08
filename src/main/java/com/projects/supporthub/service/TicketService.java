package com.projects.supporthub.service;

import java.util.List;
import java.util.UUID;
import com.projects.supporthub.model.Ticket;

public interface TicketService 
{
    public void newTicket(Ticket ticket);
    public void deleteTicketById(UUID id);
    public Ticket getTicketById(UUID id);
    public List<Ticket> getTicketByUserId(String id);
    public List<Ticket> getAllTickets();
}
