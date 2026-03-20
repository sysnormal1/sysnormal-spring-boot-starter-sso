package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.auth.filters;

import com.sysnormal.commons.core.DefaultDataSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    //private final ObjectMapper objectMapper = new ObjectMapper();

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        DefaultDataSwap body = new DefaultDataSwap();
        body.success = false;
        body.message = authException != null ? authException.getMessage() : "not authenticated";
        body.exception = authException;
        //response.getWriter().write(objectMapper.writeValueAsString(body)); //not use like this
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
