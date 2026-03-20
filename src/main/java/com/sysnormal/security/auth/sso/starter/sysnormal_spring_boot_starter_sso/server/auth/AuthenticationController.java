package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.server.auth;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_web_utils.response.ResponseUtils;
import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.auth_core.dtos.RefreshTokenRequestDTO;
import com.sysnormal.security.auth.auth_core.dtos.TokenRequestDTO;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller wich provide endpoints to auth processes
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * the auth service
     */
    @Autowired
    private AuthenticationService authenticationService;


    /**
     * login
     *
     * @param agentAuthDto the agent parameters
     * @return response entity
     */
    @PostMapping("/login")
    public ResponseEntity<DefaultDataSwap> login(@RequestBody(required = false) AgentAuthDto agentAuthDto) {
        logger.debug("requested login {}", agentAuthDto != null ? agentAuthDto.getIdentifier() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.login(agentAuthDto));
    }

    /**
     * register
     *
     * @param agentAuthDto the agent parameters
     * @return response entity
     */
    @PostMapping("/register")
    public ResponseEntity<DefaultDataSwap> register(@RequestBody(required = false) AgentAuthDto agentAuthDto) {
        logger.debug("requested register {}", agentAuthDto != null ? agentAuthDto.getIdentifier() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.register(agentAuthDto));
    }

    /**
     * check_token
     *
     * @param tokenDto the token parameters
     * @return response entity
     */
    @PostMapping("/check_token")
    public ResponseEntity<DefaultDataSwap> checkToken(@RequestBody(required = false) TokenRequestDTO tokenDto) {
        logger.debug("requested check_token {}",tokenDto != null ? tokenDto.getToken() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.checkTokenFromDto(tokenDto));
    }

    /**
     * refreshToken
     *
     * @param refreshTokenDto the refresh token parameters
     * @return response entity
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<DefaultDataSwap> refreshToken(@RequestBody(required = false) RefreshTokenRequestDTO refreshTokenDto) {
        logger.debug("requested refresh_token {}",refreshTokenDto != null ? refreshTokenDto.getRefreshToken() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.refreshTokenFromDto(refreshTokenDto));
    }

    /**
     * sendEmailRecoverPassword
     *
     * @param agentAuthDto the password recover parameters
     * @return response entity
     */
    @PostMapping("/send_email_recover_password")
    public ResponseEntity<DefaultDataSwap> sendEmailRecoverPassword(@RequestBody(required = false) AgentAuthDto agentAuthDto) {
        logger.debug("requested send_email_recover_password {}",agentAuthDto != null ? agentAuthDto.getIdentifier() + "," +  agentAuthDto.getPasswordChangeInterfacePath() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.sendEmailRecoverPasswordFromDto(agentAuthDto));
    }

    /**
     * password_change
     *
     * @param agentAuthDto the password change parameters
     * @return response entity
     */
    @PostMapping("/password_change")
    public ResponseEntity<DefaultDataSwap> passwordChange(@RequestBody(required = false) AgentAuthDto agentAuthDto) {
        logger.debug("requested password_change {}",agentAuthDto != null ? agentAuthDto.getToken() : null);
        return ResponseUtils.sendDefaultDataSwapResponse(authenticationService.passwordChangeFromDto(agentAuthDto));
    }
}
