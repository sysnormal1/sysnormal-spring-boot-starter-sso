package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.configs;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<Long> auditorAwareImpl() {
        return new AuditorAwareImpl();
    }
}
