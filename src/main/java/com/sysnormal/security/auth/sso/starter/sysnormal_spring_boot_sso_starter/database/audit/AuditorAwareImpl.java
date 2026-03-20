package com.sysnormal.security.auth.sso.starter.database.audit;

import com.sysnormal.security.auth.sso.starter.database.entities.sso.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    private static final Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<Long> getCurrentAuditor() {
        logger.debug("INIT {} {}", this.getClass().getSimpleName(), "getCurrentAuditor");
        Optional<Long> result = Optional.of(Agent.SYSTEM_ID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        logger.debug("passou xxxxxxxxxxxx 0 {}",authentication instanceof AnonymousAuthenticationToken);
        if (authentication != null) {
            logger.debug("passou xxxxxxxxxxxx 0 a {} {}",authentication.getPrincipal(), authentication.getDetails());
        }
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() != null
        ) {
            logger.debug("passou xxxxxxxxxxxx 1");
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long l) {
                result =  Optional.of(l);
            }
            if (principal instanceof String s) {
                String value = principal.toString();
                if (!value.matches("\\d+")) {
                    result = Optional.of(Long.valueOf(value));
                }
            }
        }
        logger.debug("END {} {} {}", this.getClass().getSimpleName(), "getCurrentAuditor", result.get());
        return result;
    }
}