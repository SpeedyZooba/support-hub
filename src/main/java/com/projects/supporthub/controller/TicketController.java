package com.projects.supporthub.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.model.User;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.TicketService;

@Controller
@EnableMethodSecurity
@RequestMapping("/tickets")
public class TicketController
{
    private SecurityService verifier;
    private TicketService tickets;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);

    public TicketController(SecurityService verifier, TicketService tickets)
    {
        this.verifier = verifier;
        this.tickets = tickets;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields("ticketId");
    }
    
    @GetMapping
    public String displayTicketsByUser(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        log.info("displayTicketsByUser has begun execution.");
        log.info("Retrieving the User of this session.");
        User currentUser = verifier.sessionOwnerRetrieval();
        Page<Ticket> ticketsFound = findPaginatedForUserId(page, currentUser.getUserId());
        // add the message to display when there are no tickets issued by the specified user
        if (ticketsFound.isEmpty())
        {
            log.warn("Inside the isEmpty() check conditional block.");
            model.addAttribute("empty", "No tickets issued by this user found.");
        }
        log.info("displayTicketsByUser is about to finish execution.");
        // create pagination for display
        return addPagination(page, model, ticketsFound);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("@verifier.ticketIdVerification(#ticketId)")
    public ModelAndView displayTicketById(@PathVariable("ticketId") UUID ticketId)
    {
        log.info("displayTicketById has begun execution.");
        ModelAndView mav = new ModelAndView("ticketinfo");
        Ticket ticketFound = tickets.getTicketById(ticketId);
        mav.addObject("ticket", ticketFound);
        log.info("displayTicketById is about to finish execution.");
        return mav;
    }

    @GetMapping("/new")
    public String initTicketForm(Model model)
    {
        log.info("initTicketForm has begun execution.");
        Ticket ticket = new Ticket();
        model.addAttribute("newTicket", ticket);
        log.info("initTicketForm is about to finish execution.");
        return "newticketform";
    }

    @PostMapping("/new")
    public String processTicketForm(@Valid Ticket ticket, BindingResult result, @PathVariable("userId") String userId)
    {
        log.info("processTicketForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        tickets.newTicket(ticket);
        log.info("processTicketForm is about to finish execution.");
        return "redirect:/{userId}/tickets";
    }

    @DeleteMapping("/delete/{ticketId}")
    @PreAuthorize("@verifier.ticketIdVerification(#ticketId)")
    public String deleteTicket(@PathVariable("ticketId") UUID ticketId, @PathVariable("userId") String userId)
    {
        log.info("deleteTicktet has begun execution.");
        tickets.deleteTicketById(ticketId);
        log.info("deleteTicket is about to finish execution.");
        return "redirect:/tickets";
    }

    private Page<Ticket> findPaginatedForUserId(int page, String userId)
    {
        log.info("Inside helper method findPaginatedForUserId.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").ascending());
        log.info("Helper about to terminate.");
        return tickets.getTicketByUserId(userId, pages);
    }

    private String addPagination(int page, Model model, Page<Ticket> pagination) 
    {
        log.info("Inside helper method addPagination.");
        model.addAttribute("ticketPage", pagination);
        List<Ticket> tickets = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("ticketList", tickets);
        log.info("Helper about to terminate.");
        return "/tickets/all";
    }
}