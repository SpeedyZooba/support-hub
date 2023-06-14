package com.projects.supporthub.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.model.Notice;
import com.projects.supporthub.service.NoticeService;

@Controller
@RequestMapping("/notices")
public class NoticeController 
{
    private final NoticeService notices;

    private static final Logger log = LoggerFactory.getLogger(NoticeController.class);

    public NoticeController(NoticeService notices)
    {
        this.notices = notices;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder binder)
    {
        binder.setDisallowedFields("noticeId");
    }

    @GetMapping
    public String displayAllNotices(@RequestParam(defaultValue = "1") int page, BindingResult result, Model model)
    {
        log.info("displayAllNotices has begun execution.");
        Page<Notice> noticesFound = findNoticesPaginated(page);
        // add the message to display when there are no records of any notices
        if (noticesFound.isEmpty())
        {
            log.warn("Inside the isEmpty() check conditional block.");
            model.addAttribute("empty", "No notices found.");
        }
        log.info("displayAllNotices is about to finish execution.");
        // create pagination for display
        return addPagination(page, model, noticesFound);
    }

    @GetMapping("/{noticeId}")
    public ModelAndView displayNoticeById(@PathVariable("noticeId") int noticeId)
    {
        log.info("displayNoticeById has begun execution.");
        ModelAndView mav = new ModelAndView("noticeinfo");
        Notice noticeFound = notices.getNoticeById(noticeId);
        mav.addObject("notice", noticeFound);
        log.info("displayNoticeById is about to finish execution.");
        return mav;
    }

    private Page<Notice> findNoticesPaginated(int page) 
    {
        log.debug("Inside helper method findNoticesPaginated.");
        int pageSize = 10;
        Pageable pages = PageRequest.of(page, pageSize, Sort.by("noticeDate").ascending());
        log.debug("Helper about to terminate.");
        return notices.getAllNotices(pages);
    }

    private String addPagination(int page, Model model, Page<Notice> pagination) 
    {
        log.debug("Inside helper method addPagination.");
        model.addAttribute("noticePage", pagination);
        List<Notice> notices = pagination.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagination.getTotalPages());
        model.addAttribute("totalItems", pagination.getNumberOfElements());
        model.addAttribute("noticeList", notices);
        log.debug("Helper about to terminate.");
        return "/notices/all";
    }
}