package com.projects.supporthub.service.implementation;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    public TicketServiceImpl(TicketRepository ticketRepo)
    {
        this.ticketRepo = ticketRepo;
    }

    public void newTicket(Ticket ticket)
    {
        log.debug("Inside service method newTicket.");
        log.debug("Service method newTicket calls repo method save.");
        ticketRepo.save(ticket);
    }

    public void deleteTicketById(UUID id)
    {
        log.debug("Inside service method deleteTicketById.");
        log.debug("Service method deleteTicketById is about to call repo method findById.");
        if (ticketRepo.findById(id).isEmpty())
        {
            log.error("Received invalid ticketId.");
            throw new TicketNotFoundException("Ticket not found.");
        }
        else
        {
            log.debug("Service method deleteTicketById calls repo method deleteById.");
            ticketRepo.deleteById(id);
        }
    }

    public Ticket getTicketById(UUID id)
    {
        log.debug("Inside service method getTicketById.");
        log.debug("Service method getTicketById is about to call repo method findById.");
        if (ticketRepo.findById(id).isEmpty())
        {
            log.error("Received invalid ticketId.");
            throw new TicketNotFoundException("Ticket not found.");
        }
        else
        {
            log.debug("Service method getTicketById calls repo method findById for return.");
            return ticketRepo.findById(id).get();
        }
    }

    public Page<Ticket> getTicketByUserId(String id, Pageable pageable)
    {
        log.debug("Inside service method getTicketByUserId.");
        log.debug("Service method getTicketByUserId is about to call repo method findById.");
        if (userRepo.findById(id).isEmpty())
        {
            log.error("Received invalid userId.");
            throw new UserNotFoundException("No user found for ticket display.");
        }
        else
        {
            log.debug("Service method getTicketByUserId calls repo method findByCreatorId for return.");
            return ticketRepo.findByCreatorId(id, pageable);
        }
    }

    public Page<Ticket> getAllTickets(Pageable pageable)
    {
        log.debug("Inside service method getAllTickets.");
        log.debug("Service method getAllTickets calls repo method findAll for return.");
        return ticketRepo.findAll(pageable);
    }
}