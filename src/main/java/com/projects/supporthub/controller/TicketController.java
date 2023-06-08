package com.projects.supporthub.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.service.TicketService;

@Controller
@RequestMapping("/{userId}/tickets")
public class TicketController 
{
    private final TicketService tickets;

    public TicketController(TicketService tickets)
    {
        this.tickets = tickets;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields("ticketId");
    }
    
    @GetMapping("/")
    public ModelAndView displayAllTickets(@PathVariable String userId)
    {
        ModelAndView mav = new ModelAndView("all");
        List<Ticket> ticketsFound = tickets.getTicketByUserId(userId);
        mav.addObject("tickets", ticketsFound);
        return mav;
    }

    @GetMapping("/{ticketId}")
    public ModelAndView displayTicketById(@PathVariable UUID ticketId)
    {
        ModelAndView mav = new ModelAndView("ticket");
        Ticket ticketFound = tickets.getTicketById(ticketId);
        mav.addObject("ticket", ticketFound);
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView initTicketForm()
    {
        return new ModelAndView("new", "ticket", new Ticket());
    }

    @PostMapping("/new")
    public String processTicketForm(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model)
    {
        tickets.newTicket(ticket);
        if (result.hasErrors())
        {
            return "redirect:/error";
        }
        return "/tickets/";
    }

    @DeleteMapping("/delete/{ticketId}")
    public String deleteTicket(@PathVariable UUID ticketId)
    {
        tickets.deleteTicketById(ticketId);
        return "/tickets/";
    }
}
