package com.projects.supporthub.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.TicketNotFoundException;
import com.projects.supporthub.exception.UserNotFoundException;
import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.model.User;
import com.projects.supporthub.repository.TicketRepository;
import com.projects.supporthub.repository.UserRepository;
import com.projects.supporthub.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService
{
    private final UserRepository userRepo;
    private final TicketRepository ticketRepo;

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    public TicketServiceImpl(UserRepository userRepo, TicketRepository ticketRepo)
    {
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
    }

    public void newTicket(Ticket ticket)
    {
        log.info("Inside service method newTicket.");
        log.info("Service method newTicket calls repo method save.");
        ticketRepo.save(ticket);
    }

    public Ticket getTicketById(UUID id)
    {
        log.info("Inside service method getTicketById.");
        Optional<Ticket> ticket = ticketRepo.findById(id);
        if (!ticket.isPresent() || ticket.get().getIsDeleted() == true)
        {
            log.error("Received invalid ticketId.");
            throw new TicketNotFoundException("The requested ticket was not found.");
        }
        else
        {
            log.info("Returning Ticket from the container.");
            return ticket.get();
        }
    }

    public Page<Ticket> getTicketByUserId(String id, Pageable pageable)
    {
        log.info("Inside service method getTicketByUserId.");
        log.info("Service method getTicketByUserId is about to call repo method findById.");
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent())
        {
            log.error("Received invalid userId.");
            throw new UserNotFoundException("The specified user was not found.");
        }
        else
        {
            log.info("Service method getTicketByUserId calls repo method findByCreatorId for return.");
            return ticketRepo.findByCreatorIdAndVisibility(id, pageable);
        }
    }

    public Page<Ticket> getAllTickets(Pageable pageable)
    {
        log.info("Inside service method getAllTickets.");
        log.info("Service method getAllTickets calls repo method findAll for return.");
        return ticketRepo.findByVisibility(pageable);
    }

    public List<Ticket> getLatestTickets(String userId)
    {
        Page<Ticket> results = ticketRepo.findByCreatorIdAndVisibility(userId, PageRequest.of(0, 5, Sort.by("status").ascending().and(Sort.by("createdAt").descending())));
        return results.getContent();
    }

    public List<Ticket> getAllLatestTickets()
    {
        Page<Ticket> results = ticketRepo.findByVisibility(PageRequest.of(0, 5, Sort.by("status").ascending().and(Sort.by("createdAt").descending())));
        return results.getContent();
    }
}