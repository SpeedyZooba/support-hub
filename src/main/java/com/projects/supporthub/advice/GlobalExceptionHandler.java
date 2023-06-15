package com.projects.supporthub.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler 
{
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException exception)
    {
        ModelAndView mav = new ModelAndView("error/notfound");
        mav.addObject("errorCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorMsg", HttpStatus.NOT_FOUND.getReasonPhrase());
        return mav;
    }
}