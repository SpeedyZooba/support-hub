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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.projects.supporthub.service.RoleService;
import com.projects.supporthub.service.SecurityService;
import com.projects.supporthub.service.TicketService;
import com.projects.supporthub.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/adminpanel")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController 
{
    private SecurityService verifier;
    private UserService users;
    private RoleService roles;
    private TicketService tickets;
    private NoticeService notices;

    private static final String ERROR_REDIRECTION = "redirect:/error";
    private static final PasswordEncoder encryptor = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(SecurityService verifier, UserService users, RoleService roles, TicketService tickets, NoticeService notices)
    {
        this.verifier = verifier;
        this.users = users;
        this.roles = roles;
        this.tickets = tickets;
        this.notices = notices;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request)
    {
        return request.getRequestURI();
    }

    @GetMapping
    public String adminHome()
    {
        return "adminpanel";
    }

    @GetMapping("/notices")
    public String displayAllNotices(@RequestParam(defaultValue = "1") int page, Model model)
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
        return "noticeform";
    }

    @PostMapping("/notices/new")
    public String processNoticeForm(@Valid @ModelAttribute("newNotice") Notice notice, BindingResult result)
    {
        log.info("processNoticeForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        notices.newNotice(notice);
        log.info("processNoticeForm is about to finish execution.");
        return "redirect:/adminpanel/notices";
    }

    @GetMapping("/notices/{noticeId}/update")
    public String initDescriptionUpdateForm(@PathVariable("noticeId") int noticeId, Model model)
    {
        log.info("initDescriptionNoticeForm has begun execution");
        Notice notice = notices.getNoticeById(noticeId);
        model.addAttribute("notice", notice);
        log.info("initDescriptionUpdateForm is about to finish execution.");
        return "descriptionform";
    }

    @PostMapping("/notices/{noticeId}/update")
    public String processDescriptionUpdateForm(@Valid @ModelAttribute("notice") Notice notice, BindingResult result)
    {
        log.info("processDescriptionUpdateForm is about to finish execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        notices.newNotice(notice);
        log.info("processDescriptionUpdateForm is about to finish execution.");
        return "redirect:/adminpanel/notices";
    }

    @DeleteMapping("/notices/{noticeId}/delete")
    public ResponseEntity<String> deleteNotice(@PathVariable("noticeId") int noticeId)
    {
        log.info("deleteNotice has begun execution.");
        notices.deleteNoticeById(noticeId);
        log.info("deleteTicket is about to finish execution.");
        return ResponseEntity.ok("Notice has been deleted.");
    }

    @GetMapping("/tickets")
    public String displayAllTickets(@RequestParam(defaultValue = "1") int page, Model model)
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

    @PostMapping("/tickets/{ticketId}/update")
    public ResponseEntity<String> markAsResolved(@PathVariable("ticketId") UUID ticketId)
    {
        log.info("markAsResolved has begun execution.");
        Ticket ticketToMark = tickets.getTicketById(ticketId);
        ticketToMark.setStatus(Status.ANSWERED);
        tickets.newTicket(ticketToMark);
        log.info("markAsResolved is about to finish execution.");
        return ResponseEntity.ok("Ticket has been marked.");
    }

    @DeleteMapping("/tickets/{ticketId}/delete")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") UUID ticketId)
    {
        log.info("deleteTicket has begun execution.");
        tickets.deleteTicketById(ticketId);
        log.info("deleteTicket is about to finish execution.");
        return ResponseEntity.ok("Ticket has been deleted.");
    }

    @GetMapping("/users")
    public String displayAllUsers(@RequestParam(defaultValue = "1") int page, Model model)
    {
        log.info("displayAllUsers has begun execution.");
        Page<User> usersFound = findAllUsersPaginated(page);
        // add the message to display when there are no records of any users
        if (usersFound.isEmpty())
        {
            log.info("Inside the isEmpty() check conditional block.");
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
        ModelAndView mav = new ModelAndView("userinfo");
        User userFound = users.getUserById(userId);
        String sessionOwnerId = verifier.sessionOwnerRetrieval().getUserId();
        mav.addObject("user", userFound);
        mav.addObject("ownerId", sessionOwnerId);
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
        return "newuserform";
    }

    @PostMapping("/users/new")
    public String processUserForm(@Valid @ModelAttribute("newUser") User user, @RequestParam(name = "perm", defaultValue = "false", 
                                    required = true) boolean isAdmin, BindingResult result)
    {
        log.info("processUserForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        if (isAdmin)
        {
            user.setRoles(roles.getRole("ROLE_ADMIN"));
        }
        else
        {
            user.setRoles(roles.getRole("ROLE_USER"));
        }
        user.setPassword(encryptor.encode(user.getPassword()));
        user.setFirstLogin(true);
        users.newUser(user);
        log.info("processUserForm is about to finish execution.");
        return "redirect:/adminpanel/users/" + user.getUserId();
    }

    @GetMapping("/users/{userId}/update")
    public String initUserUpdateForm(@PathVariable("userId") String userId, Model model)
    {
        log.info("initUserUpdateForm has begun execution.");
        User user = users.getUserById(userId);
        model.addAttribute("user", user);
        log.info("initUserUpdateForm is about to finish execution.");
        return "updateuserform";
    }

    @PostMapping("/users/{userId}/update")
    public String processUserUpdateForm(@Valid @ModelAttribute("user") User user, @RequestParam(name = "perm", defaultValue = "false", 
                                        required = true) boolean isAdmin, BindingResult result)
    {
        log.info("processUserUpdateForm has begun execution.");
        if (result.hasErrors())
        {
            log.error("A binding error has occurred.");
            return ERROR_REDIRECTION;
        }
        if (isAdmin)
        {
            user.setRoles(roles.getRole("ROLE_ADMIN"));
        }
        else
        {
            user.setRoles(roles.getRole("ROLE_USER"));
        }
        users.newUser(user);
        log.info("processUserUpdateForm is about to finish execution.");
        return "redirect:/adminpanel/users/" + user.getUserId();
    }

    @DeleteMapping("/users/{userId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId)
    {
        if (verifier.userIdVerification(userId))
        {
            return ResponseEntity.status(403).body("Self-removal is not permitted.");
        }
        log.info("deleteUser has begun execution.");
        users.deleteUserById(userId);
        log.info("deleteUser is about to finish execution.");
        return ResponseEntity.ok("User has been deleted.");
    }

    private Page<Notice> findAllNoticesPaginated(int page) 
    {
        log.info("Inside helper mehtod findAllNoticesPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("noticeDate").ascending());
        log.info("Helper about to terminate.");
        return notices.getAllNotices(pages);
    }

    private String addNoticePagination(int page, Model model, Page<Notice> pagination) 
    {
        log.info("Inside helper method addNoticePagination.");
        model.addAttribute("noticePage", pagination);
        List<Notice> notices = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("noticeList", notices);
        model.addAttribute("pageURL", "/adminpanel/notices");
        log.info("Helper about to terminate.");
        return "/notices";
    }

    private Page<Ticket> findAllTicketsPaginated(int page) 
    {
        log.info("Inside helper method findAllTicketsPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").ascending());
        log.info("Helper about to terminate.");
        return tickets.getAllTickets(pages);
    }

    private String addTicketPagination(int page, Model model, Page<Ticket> pagination) 
    {
        log.info("Inside helper method addTicketPagination.");
        model.addAttribute("ticketPage", pagination);
        List<Ticket> tickets = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("ticketList", tickets);
        model.addAttribute("pageURL", "/adminpanel/tickets");
        log.info("Helper about to terminate.");
        return "/tickets";
    }

    private Page<User> findAllUsersPaginated(int page) 
    {
        log.info("Inside helper method findAllUsersPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page - 1, pageSize, Sort.by("lastName").ascending());
        log.info("Helper about to terminate.");
        return users.getAllUsers(pages);
    }

    private String addUserPagination(int page, Model model, Page<User> pagination) 
    {
        log.info("Inside helper method addUserPagination.");
        model.addAttribute("userPage", pagination);
        List<User> users = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("userList", users);
        log.info("Helper about to terminate.");
        return "/users";
    }
}