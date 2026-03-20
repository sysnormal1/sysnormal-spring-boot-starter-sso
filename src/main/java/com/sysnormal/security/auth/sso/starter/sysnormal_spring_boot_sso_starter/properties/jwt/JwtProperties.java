package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.jwt;

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
public class JwtProperties {

    private boolean enabled = true;
    //private String secretKey = "f324a2deac2fe6c1b8585fd3bae1df33956d6a918cfeffcbf772cb2ec4001bf8"; deprecated
    private String privateKeyPath;
    private String publicKeyPath;
    private Long defaultTokenExpiration = 60000L;
    private Long defaultRefreshTokenExpiration = 300000L;
}