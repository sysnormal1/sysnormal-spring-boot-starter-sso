package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.configs;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt.JwtSsoService;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso_client_protector.services.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityAutoConfiguration
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({JwtProperties.class})
public class JwtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JwtAutoConfiguration.class);

    /** ⚠️ IMPORTANT:
    * {@code @important} This bean must be defined via @Bean (not @Service)
    * to avoid multiple candidates when extending JwtService (e.g. JwtSsoService).
     */
    @Bean
    @ConditionalOnMissingBean({JwtSsoService.class, JwtService.class})
    public JwtService jwtService(JwtProperties jwtProperties) {
        return new JwtSsoService(jwtProperties);
    }
}
