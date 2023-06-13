package com.projects.supporthub.exception;

public class NoticeNotFoundException extends RuntimeException
{
    public NoticeNotFoundException()
    {

    }

    public NoticeNotFoundException(String message)
    {
        super(message);
    }
}