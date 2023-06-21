package com.projects.supporthub.exception;

public class TicketNotFoundException extends RuntimeException
{
    public TicketNotFoundException(String message)
    {
        super(message);
    }    
}