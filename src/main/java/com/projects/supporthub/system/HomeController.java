package com.projects.supporthub.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.projects.supporthub.model.Notice;
import com.projects.supporthub.model.Ticket;
import com.projects.supporthub.model.User;
import com.projects.supporthub.security.SecurityService;
import com.projects.supporthub.service.NoticeService;
import com.projects.supporthub.service.TicketService;
@Controller
@EnableMethodSecurity
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class HomeController
{
    private SecurityService verifier;
    private NoticeService notices;
    private TicketService tickets;

    public HomeController(SecurityService verifier, NoticeService notices, TicketService tickets)
    {
        this.notices = notices;
        this.tickets = tickets;
        this.verifier = verifier;
    }

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    
    @GetMapping("/home")
    public String homepage(Model model)
    {
        User user = verifier.sessionOwnerRetrieval();
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("department", user.getDepartment());
        model.addAttribute("title", user.getTitle());

        List<Ticket> ticketList = tickets.getLatestTickets(user.getUserId());
        model.addAttribute("ticketList", ticketList);

        List<Notice> noticeList = notices.getLatestNotices();
        model.addAttribute("noticeList", noticeList);

        log.info("User homepage initiated.");
        return "home";
    }

    @GetMapping("/")
    public String goHome()
    {
        return "redirect:/home";
    }
}