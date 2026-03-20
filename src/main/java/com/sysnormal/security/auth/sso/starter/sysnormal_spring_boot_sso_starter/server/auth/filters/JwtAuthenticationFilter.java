package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.auth.filters;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.security.SecurityProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.jwt.JwtSsoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 10)
//@EnableConfigurationProperties(SecurityProperties.class)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtSsoService jwtSsoService;

    private final SecurityProperties securityProperties;

    public JwtAuthenticationFilter(JwtSsoService jwtSsoService, SecurityProperties securityProperties) {
        this.jwtSsoService = jwtSsoService;
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
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try {
            Claims claims = jwtSsoService.getClaims(token);
            Long agentId = claims.get("agentId", Long.class);

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
            logger.debug("xxxx 7 setted authentication {} {} {}",authentication.getPrincipal(), authentication.getDetails(), claims);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("expired token",e);
        } catch (JwtException e) {
            throw new JwtAuthenticationException("invalid token",e);
        }
        logger.debug("END {}.{} {}",this.getClass().getSimpleName(), "doFilterInternal",request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
