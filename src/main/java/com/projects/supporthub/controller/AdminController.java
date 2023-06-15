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
import com.projects.supporthub.utils.PasswordEncryptionUtil;

@Controller
@RequestMapping("/adminpanel")
public class AdminController 
{
    private final UserService users;
    private final TicketService tickets;
    private final NoticeService notices;
    private final PasswordEncryptionUtil encryptor;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final String[] BLACKLIST = {"ticketId", "noticeId", "userId", "email", "passwordHash"};
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService users, TicketService tickets, NoticeService notices, PasswordEncryptionUtil encryptor)
    {
        this.users = users;
        this.tickets = tickets;
        this.notices = notices;
        this.encryptor = encryptor;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields(BLACKLIST);
    }

    @GetMapping("/notices")
    public String displayAllNotices(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        log.info("displayAllNotices has begun execution.");
        Page<Notice> noticesFound = findAllNoticesPaginated(page);
        // add the message to display when there are no records of any notices
        if (noticesFound.isEmpty())
        {
            log.warn("Inside the isEmpty() check conditional block.");
            model.addAttribute("empty", "No notices found.");
        }
        log.info("displayAllNotices is about to finish execution.");
        // create pagination for display
        return addNoticePagination(page, model, noticesFound);
    }

    @GetMapping("/notices/{noticeId}")
    public ModelAndView displayNoticeById(@PathVariable("noticeId") int noticeId)
    {
        log.info("displayNoticeById has begun execution.");
        ModelAndView mav = new ModelAndView("noticeinfo");
        Notice noticeFound = notices.getNoticeById(noticeId);
        mav.addObject("notice", noticeFound);
        log.info("displayNoticeById is about to finish execution.");
        return mav;
    }
    
    @GetMapping("/notices/new")
    public String initNoticeForm(Model model)
    {
        log.info("initNoticeForm has begun execution.");
        Notice notice = new Notice();
        model.addAttribute("newNotice", notice);
        log.info("initNoticeForm is about to finish execution.");
        return "/newnoticeform";
    }

    @PostMapping("/notices/new")
    public String processNoticeForm(@Valid Notice notice, BindingResult result)
    {
        log.info("processNoticeForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        notices.newNotice(notice);
        log.info("processNoticeForm is about to finish execution.");
        return "/notices";
    }

    @GetMapping("/tickets")
    public String displayAllTickets(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        log.info("displayAllTickets has begun execution.");
        Page<Ticket> ticketsFound = findAllTicketsPaginated(page);
        // add the message to display when there are no records of any tickets
        if (ticketsFound.isEmpty())
        {
            log.warn("Inside the isEmpty() check conditional block.");
            model.addAttribute("empty", "No tickets found.");
        }
        log.info("displayAllTickets is about to finish execution.");
        // create pagination for display
        return addTicketPagination(page, model, ticketsFound);
    }

    @GetMapping("/tickets/{ticketId}")
    public ModelAndView displayTicketById(@PathVariable("ticketId") UUID ticketId)
    {
        log.info("displayTicketById has begun execution.");
        ModelAndView mav = new ModelAndView("ticketinfo");
        Ticket ticketFound = tickets.getTicketById(ticketId);
        mav.addObject("ticket", ticketFound);
        log.info("displayTicketById is about to finish execution.");
        return mav;
    }

    @GetMapping("/tickets/{ticketId}/update")
    public String initUpdateForm(@PathVariable("ticketId") UUID ticketId, Model model)
    {
        log.info("initUpdateForm has begun execution.");
        Ticket ticket = tickets.getTicketById(ticketId);
        model.addAttribute("response", ticket);
        log.info("initUpdateForm is about to finish execution.");
        return "/response";
    }

    @PostMapping("/tickets/{tickedId}/update")
    public String processUpdateForm(@Valid Ticket ticket, BindingResult result)
    {
        log.info("processUpdateForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        ticket.setStatus(Status.ANSWERED);
        tickets.newTicket(ticket);
        log.info("processUpdateForm is about to finish execution.");
        return "/tickets";
    }

    @DeleteMapping("/tickets/{ticketId}/delete")
    public String deleteTicket(@PathVariable("ticketId") UUID ticketId)
    {
        log.info("deleteTicket has begun execution.");
        tickets.deleteTicketById(ticketId);
        log.info("deleteTicket is about to finish execution.");
        return "/tickets";
    }

    @GetMapping("/users")
    public String displayAllUsers(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        log.info("displayAllUsers has begun execution.");
        Page<User> usersFound = findAllUsersPaginated(page);
        // add the message to display when there are no records of any users
        if (usersFound.isEmpty())
        {
            log.debug("Inside the isEmpty() check conditional block.");
            model.addAttribute("empty", "No users found.");
        }
        log.info("displayAllUsers is about to finish execution.");
        // create pagination for display
        return addUserPagination(page, model, usersFound);
    }

    @GetMapping("/users/{userId}")
    public ModelAndView displayUserById(@PathVariable("userId") String userId)
    {
        log.info("displayUserById has begun execution.");
        ModelAndView mav = new ModelAndView("userdetails");
        User userFound = users.getUserById(userId);
        mav.addObject("user", userFound);
        log.info("displayUserById is about to finish execution.");
        return mav;
    }

    @GetMapping("/users/new")
    public String initUserForm(Model model)
    {
        log.info("initUserForm has begun execution.");
        User user = new User();
        model.addAttribute("newUser", user);
        log.info("initUserForm is about to finish execution.");
        return "/newuserform";
    }

    @PostMapping("/users/new")
    public String processUserForm(@Valid User user, BindingResult result)
    {
        log.info("processUserForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        user.setPassword(encryptor.encryptPassword(user.getPassword()));
        users.newUser(user);
        log.info("processUserForm is about to finish execution.");
        return new StringBuilder().append("/users/").append(user.getUserId()).toString();
    }

    @DeleteMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") String userId)
    {
        log.info("deleteUser has begun execution.");
        users.deleteUserById(userId);
        log.info("deleteUser is about to finish execution.");
        return "/users";
    }

    private Page<Notice> findAllNoticesPaginated(int page) 
    {
        log.debug("Inside helper mehtod findAllNoticesPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page, pageSize, Sort.by("noticeDate").ascending());
        log.debug("Helper about to terminate.");
        return notices.getAllNotices(pages);
    }

    private String addNoticePagination(int page, Model model, Page<Notice> pagination) 
    {
        log.debug("Inside helper method addNoticePagination.");
        model.addAttribute("noticePage", pagination);
        List<Notice> notices = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("noticeList", notices);
        log.debug("Helper about to terminate.");
        return "/notices/all";
    }

    private Page<Ticket> findAllTicketsPaginated(int page) 
    {
        log.debug("Inside helper method findAllTicketsPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").ascending());
        log.debug("Helper about to terminate.");
        return tickets.getAllTickets(pages);
    }

    private String addTicketPagination(int page, Model model, Page<Ticket> pagination) 
    {
        log.debug("Inside helper method addTicketPagination.");
        model.addAttribute("ticketPages", pagination);
        List<Ticket> tickets = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("ticketList", tickets);
        log.debug("Helper about to terminate.");
        return "/tickets/all";
    }

    private Page<User> findAllUsersPaginated(int page) 
    {
        log.debug("Inside helper method findAllUsersPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("lastName").ascending());
        log.debug("Helper about to terminate.");
        return users.getAllUsers(pages);
    }

    private String addUserPagination(int page, Model model, Page<User> pagination) 
    {
        log.debug("Inside helper method addUserPagination.");
        model.addAttribute("userPages", pagination);
        List<User> users = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("userList", users);
        log.debug("Helper about to terminate.");
        return "/users/all";
    }
}