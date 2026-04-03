package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.configs;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtSsoProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt.JwtSsoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * SecurityAutoConfiguration
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({JwtSsoProperties.class})
public class JwtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JwtAutoConfiguration.class);

    /** ⚠️ IMPORTANT:
    * {@code @important} This bean must be defined via @Bean (not @Service)
    * to avoid multiple candidates when extending JwtService (e.g. JwtSsoService).
     */
    @Bean(name = "jwtSsoService")
    @ConditionalOnMissingBean(name = "jwtSsoService")
    public JwtSsoService jwtSsoService(JwtSsoProperties jwtSsoProperties) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return new JwtSsoService(jwtSsoProperties);
    }
}
