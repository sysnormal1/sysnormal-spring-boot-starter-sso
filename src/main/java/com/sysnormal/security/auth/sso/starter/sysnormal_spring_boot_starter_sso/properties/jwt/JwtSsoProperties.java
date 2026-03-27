package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso_client_protector.properties.jwt.JwtSsoClientProtectorProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt properties
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.jwt")
@Getter
@Setter
public class JwtSsoProperties extends JwtSsoClientProtectorProperties {
    private String privateKeyPath;
    private Long defaultTokenExpiration = 60000L;
    private Long defaultRefreshTokenExpiration = 300000L;
}