package com.projects.supporthub.exception;

public class TicketNotFoundException extends RuntimeException
{
    private String message;

    public TicketNotFoundException()
    {

    }

    public TicketNotFoundException(String message)
    {
        super(message);
        this.message = message;
    }
}