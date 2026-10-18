package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.auth.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.auth.github")
@ConditionalOnProperty(prefix = "spring.auth.github", name = "enabled", havingValue = "true")
public class GitHubAuthProperties {
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