package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.server.auth.google;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.auth.google.GoogleAuthService;
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
@RequestMapping("/auth/google")
@ConditionalOnProperty(prefix = "spring.auth.google", name = "enabled", havingValue = "true")
public class GoogleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthController.class);


    @Autowired
    private GoogleAuthService googleAuthService;


    /**
     * register
     *
     * @return response entity
     */
    @PostMapping("/get_login_url")
    public ResponseEntity<DefaultDataSwap> getLoginUrl() {
        logger.debug("requested get_login_url");
        return ResponseUtils.sendDefaultDataSwapResponse(googleAuthService.getLoginUrl());
    }

    @PostMapping("/handle_code")
    public ResponseEntity<?> handleCode(@RequestBody(required = false) AgentAuthDto agentAuthDto) {
        logger.debug("requested handle_code");
        return ResponseUtils.sendDefaultDataSwapResponse(googleAuthService.handleCode(agentAuthDto));
    }
}
