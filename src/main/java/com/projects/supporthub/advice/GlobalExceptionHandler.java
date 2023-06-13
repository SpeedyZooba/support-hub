package com.projects.supporthub.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.exception.NoticeNotFoundException;
import com.projects.supporthub.exception.TicketNotFoundException;
import com.projects.supporthub.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler 
{
    @ExceptionHandler(value = NoticeNotFoundException.class)
    public ModelAndView handleNoticeNotFoundException(NoticeNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("notfound");
        mav.addObject("errorCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorMsg", "Notice not found.");
        return mav;
    }

    @ExceptionHandler(value = TicketNotFoundException.class)
    public ModelAndView handleTicketNotFoundException(TicketNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("notfound");
        mav.addObject("errorCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorMsg", "Ticket not found.");
        return mav;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("notfound");
        mav.addObject("errorCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorMsg", "User not found.");
        return mav;
    }
}