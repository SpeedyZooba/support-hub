package com.projects.supporthub.service.implementation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.TicketNotFoundException;
import com.projects.supporthub.exception.UserNotFoundException;
import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService
{
    private UserRepository userRepo;
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
        if (ticketRepo.findById(id).isEmpty())
        {
            throw new TicketNotFoundException("Ticket not found.");
        }
        else
        {
            ticketRepo.deleteById(id);
        }
    }

    public Ticket getTicketById(UUID id)
    {
        if (ticketRepo.findById(id).isEmpty())
        {
            throw new TicketNotFoundException("Ticket not found.");
        }
        else
        {
            return ticketRepo.findById(id).get();
        }
    }

    public Page<Ticket> getTicketByUserId(String id, Pageable pageable)
    {
        if (userRepo.findById(id).isEmpty())
        {
            throw new UserNotFoundException("No user found for ticket display.");
        }
        else
        {
            return ticketRepo.findByCreatorId(id, pageable);
        }
    }

    public Page<Ticket> getAllTickets(Pageable pageable)
    {
        return ticketRepo.findAll(pageable);
    }
}