package com.projects.supporthub.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.projects.supporthub.service.TicketService;

@Controller
@RequestMapping("/{userId}/tickets")
public class TicketController 
{
    private final TicketService tickets;

    private static final String ERROR_REDIRECTION = "redirect:/error";

    public TicketController(TicketService tickets)
    {
        this.tickets = tickets;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields("ticketId");
    }
    
    @GetMapping
    public String displayTicketsByUser(@RequestParam(defaultValue = "1") int page, @PathVariable("userId") String userId, BindingResult result, Model model)
    {
        Page<Ticket> ticketsFound = findPaginatedForUserId(page, userId);
        // add the message to display when there are no tickets issued by the specified user
        if (ticketsFound.isEmpty())
        {
            model.addAttribute("empty", "No tickets issued by this user found.");
        }
        // create pagination for display
        return addPagination(page, model, ticketsFound);
    }

    @GetMapping("/{ticketId}")
    public ModelAndView displayTicketById(@PathVariable("userId") String userId, @PathVariable("ticketId") UUID ticketId)
    {
        ModelAndView mav = new ModelAndView("ticketinfo");
        Ticket ticketFound = tickets.getTicketById(ticketId);
        if (!ticketFound.getCreatedBy().equals(userId))
        {
            return new ModelAndView(ERROR_REDIRECTION);
        }
        mav.addObject("ticket", ticketFound);
        return mav;
    }

    @GetMapping("/new")
    public String initTicketForm(Model model)
    {
        Ticket ticket = new Ticket();
        model.addAttribute("newTicket", ticket);
        return "/newticketform";
    }

    @PostMapping("/new")
    public String processTicketForm(@Valid Ticket ticket, BindingResult result)
    {
        if (result.hasErrors())
        {
            return ERROR_REDIRECTION;
        }
        tickets.newTicket(ticket);
        return "redirect:/tickets";
    }

    @DeleteMapping("/delete/{ticketId}")
    public String deleteTicket(@PathVariable("ticketId") UUID ticketId)
    {
        tickets.deleteTicketById(ticketId);
        return "/tickets";
    }

    private Page<Ticket> findPaginatedForUserId(int page, String userId)
    {
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").ascending());
        return tickets.getTicketByUserId(userId, pages);
    }

    private String addPagination(int page, Model model, Page<Ticket> pagination) 
    {
        model.addAttribute("ticketPage", pagination);
        List<Ticket> tickets = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("ticketList", tickets);
        return "/tickets/all";
    }
}