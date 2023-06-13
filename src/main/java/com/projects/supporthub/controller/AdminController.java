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

import com.projects.supporthub.model.Notice;
import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.model.User;
import com.projects.supporthub.model.Ticket.Status;
import com.projects.supporthub.service.NoticeService;
import com.projects.supporthub.service.TicketService;
import com.projects.supporthub.service.UserService;

@Controller
@RequestMapping("/adminpanel")
public class AdminController 
{
    private final UserService users;
    private final TicketService tickets;
    private final NoticeService notices;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final String[] BLACKLIST = {"ticketId", "noticeId", "userId", "email", "passwordHash"};

    public AdminController(UserService users, TicketService tickets, NoticeService notices)
    {
        this.users = users;
        this.tickets = tickets;
        this.notices = notices;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields(BLACKLIST);
    }

    @GetMapping("/notices")
    public String displayAllNotices(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        Page<Notice> noticesFound = findAllNoticesPaginated(page);
        // add the message to display when there are no records of any notices
        if (noticesFound.isEmpty())
        {
            model.addAttribute("empty", "No notices found.");
        }
        // create pagination for display
        return addNoticePagination(page, model, noticesFound);
    }

    @GetMapping("/notices/{noticeId}")
    public ModelAndView displayNoticeById(@PathVariable("noticeId") int noticeId)
    {
        ModelAndView mav = new ModelAndView("noticeinfo");
        Notice noticeFound = notices.getNoticeById(noticeId);
        if (noticeFound == null)
        {
            return new ModelAndView(ERROR_REDIRECTION);
        }
        mav.addObject("notice", noticeFound);
        return mav;
    }
    
    @GetMapping("/notices/new")
    public String initNoticeForm(Model model)
    {
        Notice notice = new Notice();
        model.addAttribute("newNotice", notice);
        return "/newnoticeform";
    }

    @PostMapping("/notices/new")
    public String processNoticeForm(@Valid Notice notice, BindingResult result)
    {
        if (result.hasErrors())
        {
            return ERROR_REDIRECTION;
        }
        notices.newNotice(notice);
        return "/notices";
    }

    @GetMapping("/tickets")
    public String displayAllTickets(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        Page<Ticket> ticketsFound = findAllTicketsPaginated(page);
        // add the message to display when there are no records of any tickets
        if (ticketsFound.isEmpty())
        {
            model.addAttribute("empty", "No tickets found.");
        }
        // create pagination for display
        return addTicketPagination(page, model, ticketsFound);
    }

    @GetMapping("/tickets/{ticketId}")
    public ModelAndView displayTicketById(@PathVariable("ticketId") UUID ticketId)
    {
        ModelAndView mav = new ModelAndView("ticketinfo");
        Ticket ticketFound = tickets.getTicketById(ticketId);
        if (ticketFound == null)
        {
            return new ModelAndView(ERROR_REDIRECTION);
        }
        mav.addObject("ticket", ticketFound);
        return mav;
    }

    @GetMapping("/tickets/{ticketId}/update")
    public String initUpdateForm(@PathVariable("ticketId") UUID ticketId, Model model)
    {
        Ticket ticket = tickets.getTicketById(ticketId);
        model.addAttribute("response", ticket);
        return "/response";
    }

    @PostMapping("/tickets/{tickedId}/update")
    public String processUpdateForm(@Valid Ticket ticket, BindingResult result)
    {
        if (result.hasErrors())
        {
            return ERROR_REDIRECTION;
        }
        ticket.setStatus(Status.ANSWERED);
        tickets.newTicket(ticket);
        return "/tickets";
    }

    @DeleteMapping("/tickets/{ticketId}/delete")
    public String deleteTicket(@PathVariable("ticketId") UUID ticketId)
    {
        if (tickets.getTicketById(ticketId) == null)
        {
            return ERROR_REDIRECTION;
        }
        tickets.deleteTicketById(ticketId);
        return "/tickets";
    }

    @GetMapping("/users")
    public String displayAllUsers(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        Page<User> usersFound = findAllUsersPaginated(page);
        // add the message to display when there are no records of any users
        if (usersFound.isEmpty())
        {
            model.addAttribute("empty", "No users found.");
        }
        // create pagination for display
        return addUserPagination(page, model, usersFound);
    }

    @GetMapping("/users/{userId}")
    public ModelAndView displayUserById(@PathVariable("userId") String userId)
    {
        ModelAndView mav = new ModelAndView("userdetails");
        User userFound = users.getUserById(userId);
        if (userFound == null)
        {
            return new ModelAndView(ERROR_REDIRECTION);
        }
        mav.addObject("user", userFound);
        return mav;
    }

    @GetMapping("/users/new")
    public String initUserForm(Model model)
    {
        User user = new User();
        model.addAttribute("newUser", user);
        return "/newuserform";
    }

    @PostMapping("/users/new")
    public String processUserForm(@Valid User user, BindingResult result)
    {
        if (result.hasErrors())
        {
            return ERROR_REDIRECTION;
        }
        users.newUser(user);
        return new StringBuilder().append("/users/").append(user.getUserId()).toString();
    }

    @DeleteMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") String userId)
    {
        if (users.getUserById(userId) == null)
        {
            return ERROR_REDIRECTION;
        }
        users.deleteUserById(userId);
        return "/users";
    }

    private Page<Notice> findAllNoticesPaginated(int page) 
    {
        int pageSize = 10;
        Pageable pages = PageRequest.of(page, pageSize, Sort.by("noticeDate").ascending());
        return notices.getAllNotices(pages);
    }

    private String addNoticePagination(int page, Model model, Page<Notice> pagination) 
    {
        model.addAttribute("noticePage", pagination);
        List<Notice> notices = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("noticeList", notices);
        return "/notices/all";
    }

    private Page<Ticket> findAllTicketsPaginated(int page) 
    {
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").ascending());
        return tickets.getAllTickets(pages);
    }

    private String addTicketPagination(int page, Model model, Page<Ticket> pagination) 
    {
        model.addAttribute("ticketPages", pagination);
        List<Ticket> tickets = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("ticketList", tickets);
        return "/tickets/all";
    }

    private Page<User> findAllUsersPaginated(int page) 
    {
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("lastName").ascending());
        return users.getAllUsers(pages);
    }

    private String addUserPagination(int page, Model model, Page<User> pagination) 
    {
        model.addAttribute("userPages", pagination);
        List<User> users = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("userList", users);
        return "/users/all";
    }
}