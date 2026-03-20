package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.auth.filters;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg) {
        super(msg);
    }
    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
