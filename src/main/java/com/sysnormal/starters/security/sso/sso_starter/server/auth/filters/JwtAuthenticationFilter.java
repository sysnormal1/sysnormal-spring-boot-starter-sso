package com.sysnormal.starters.security.sso.sso_starter.server.auth.filters;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.helpers.http.HttpUtils;
import com.sysnormal.starters.security.sso.sso_starter.properties.security.SecurityProperties;
import com.sysnormal.starters.security.sso.sso_starter.services.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.List;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 10)
//@EnableConfigurationProperties(SecurityProperties.class)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    private final SecurityProperties securityProperties;

    public JwtAuthenticationFilter(JwtService jwtService, SecurityProperties securityProperties) {
        this.jwtService = jwtService;
        this.securityProperties = securityProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securityProperties.getPublicEndPoints().contains(request.getRequestURI());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("INIT {}.{} {}",this.getClass().getSimpleName(), "doFilterInternal",request.getRequestURI());
        logger.debug("xxxx 1");
        String header = request.getHeader("Authorization");
        logger.debug("xxxx 2");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        logger.debug("xxxx 3");
        String token = header.substring(7);
        logger.debug("xxxx 4");
        try {
            Claims claims = jwtService.getClaims(token);
            logger.debug("xxxx 5");
            Long agentId = claims.get("agentId", Long.class);
            logger.debug("xxxx 6");

            // Você pode criar uma implementação própria de UserDetails se quiser
            List<GrantedAuthority> authorities = List.of();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            agentId,
                            null,
                            authorities
                    );
            authentication.setDetails(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("xxxx 7");
        } catch (ExpiredJwtException e) {
            logger.debug("xxxx 8");
            throw new JwtAuthenticationException("expired token",e);
        } catch (JwtException e) {
            throw new JwtAuthenticationException("invalid token",e);
        }
        logger.debug("END {}.{} {}",this.getClass().getSimpleName(), "doFilterInternal",request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
