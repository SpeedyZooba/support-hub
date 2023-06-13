package com.projects.supporthub.exception;

public class NoticeNotFoundException extends RuntimeException
{
    private String message;

    public NoticeNotFoundException()
    {

    }

    public NoticeNotFoundException(String message)
    {
        super(message);
        this.message = message;
    }
}