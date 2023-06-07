package com.projects.supporthub.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.model.UserTicketLink;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserTicketLinkRepository;
import com.projects.supporthub.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService
{
    private TicketRepository ticketRepo;
    private UserTicketLinkRepository linkRepo;

    public TicketServiceImpl(TicketRepository ticketRepo, UserTicketLinkRepository linkRepo)
    {
        this.ticketRepo = ticketRepo;
        this.linkRepo = linkRepo;
    }

    public void newTicket(Ticket ticket)
    {
        ticketRepo.save(ticket);
        UserTicketLink newLink = new UserTicketLink();
        newLink.setUserId(ticket.getIssuerId());
        newLink.setTicketId(ticket.getTicketId());
        linkRepo.save(newLink);
    }

    public void deleteTicketById(UUID id)
    {
        ticketRepo.deleteById(id);
        linkRepo.deleteByTicketId(id);
    }

    public Ticket getTicketById(UUID id)
    {
        return ticketRepo.findById(id).get();
    }

    public List<Ticket> getAllTickets()
    {
        return ticketRepo.findAll();
    }
}
