package com.projects.supporthub.exception;

public class BadCredentialException extends RuntimeException
{
    public BadCredentialException(String message)
    {
        super(message);
    }
}