package com.projects.supporthub.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService
{
    private TicketRepository ticketRepo;

    public TicketServiceImpl(TicketRepository ticketRepo)
    {
        this.ticketRepo = ticketRepo;
    }

    public void newTicket(Ticket ticket)
    {
        ticketRepo.save(ticket);
    }

    public void deleteTicketById(UUID id)
    {
        ticketRepo.deleteById(id);
    }

    public Ticket getTicketById(UUID id)
    {
        return ticketRepo.findById(id).get();
    }

    public List<Ticket> getTicketByUserId(String id)
    {
        return ticketRepo.findByCreatorId(id);
    }

    public List<Ticket> getAllTickets()
    {
        return ticketRepo.findAll();
    }
}
