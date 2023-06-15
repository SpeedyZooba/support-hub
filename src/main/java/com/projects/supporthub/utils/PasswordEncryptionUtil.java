package com.projects.supporthub.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptionUtil 
{
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public String encryptPassword(String password)
    {
        return passwordEncoder.encode(password);
    }

    public boolean matchPasswords(String plain, String encoded)
    {
        return passwordEncoder.matches(plain, encoded);
    }
}