package com.sysnormal.security.auth.sso.starter.server.auth.github;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.services.auth.github.GitHubAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/github")
@ConditionalOnProperty(prefix = "spring.auth.github", name = "enabled", havingValue = "true")
public class GitHubAuthController {

    private static final Logger logger = LoggerFactory.getLogger(GitHubAuthController.class);

    @Autowired
    private GitHubAuthService gitHubAuthService;

    /**
     * Get GitHub login URL
     *
     * @return response entity with login URL
     */
    @PostMapping("/get_login_url")
    public ResponseEntity<DefaultDataSwap> getLoginUrl() {
        logger.debug("requested get_login_url for GitHub");
        return ResponseUtils.sendDefaultDataSwapResponse(gitHubAuthService.getLoginUrl());
    }

    /**
     * Handle OAuth callback code
     *
     * @param agentAuthDto DTO containing code and redirect URI
     * @return response entity with authentication result
     */
    @PostMapping("/handle_code")
    public ResponseEntity<?> handleCode(@RequestBody AgentAuthDto agentAuthDto) {
        logger.debug("requested handle_code for GitHub");
        return ResponseUtils.sendDefaultDataSwapResponse(gitHubAuthService.handleCode(agentAuthDto));
    }
}