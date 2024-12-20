package com.example.slackproject.Exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}