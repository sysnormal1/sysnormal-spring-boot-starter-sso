package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.auth.google;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.auth.google")
@ConditionalOnProperty(prefix = "spring.auth.google", name = "enabled", havingValue = "true")
public class GoogleAuthProperties {
    private boolean enabled = false;
    private String clientId = null;
    private String clientSecret = null;
    /**
     * Ignorado. O {@code redirectUri} vem do front, no corpo do {@code get_login_url}
     * e do {@code handle_code}; fica só para o serviço avisar no log quem ainda o configura.
     */
    @Deprecated
    private String redirectUri = null;
}