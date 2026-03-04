package com.sysnormal.starters.security.sso.sso_starter.server.auth.filters;

import com.sysnormal.libs.commons.DefaultDataSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        DefaultDataSwap body = new DefaultDataSwap();
        body.success = false;
        body.message = accessDeniedException != null ? accessDeniedException.getMessage() : "not authorized";
        body.exception = accessDeniedException;
        //response.getWriter().write(objectMapper.writeValueAsString(body)); //not use like this
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
