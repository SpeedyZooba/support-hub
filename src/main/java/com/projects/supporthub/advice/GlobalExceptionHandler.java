package com.projects.supporthub.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.projects.supporthub.exception.BadCredentialException;
import com.projects.supporthub.exception.NoticeNotFoundException;
import com.projects.supporthub.exception.TicketNotFoundException;
import com.projects.supporthub.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler 
{
    @ExceptionHandler(value = UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", HttpStatus.NOT_FOUND.value());
        mav.addObject("cause", HttpStatus.NOT_FOUND.getReasonPhrase());
        mav.addObject("message", exception.getMessage());
        return mav;
    }

    @ExceptionHandler(value = TicketNotFoundException.class)
    public ModelAndView handleTicketNotFoundException(TicketNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", HttpStatus.NOT_FOUND.value());
        mav.addObject("cause", HttpStatus.NOT_FOUND.getReasonPhrase());
        mav.addObject("message", exception.getMessage());
        return mav;
    }

    @ExceptionHandler(value = NoticeNotFoundException.class)
    public ModelAndView handleNoticeNotFoundException(NoticeNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", HttpStatus.NOT_FOUND.value());
        mav.addObject("cause", HttpStatus.NOT_FOUND.getReasonPhrase());
        mav.addObject("message", exception.getMessage());
        return mav;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(Exception exception)
    {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", HttpStatus.FORBIDDEN.value());
        mav.addObject("cause", HttpStatus.FORBIDDEN.getReasonPhrase());
        mav.addObject("message", "You do not have permission to view this page.");
        return mav;
    }

    @ExceptionHandler(value = BadCredentialException.class)
    public ModelAndView handleBadCredentialException(BadCredentialException exception)
    {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("failure", "Invalid username or password.");
        return mav;
    }
}