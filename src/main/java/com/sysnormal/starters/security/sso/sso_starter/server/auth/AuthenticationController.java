package com.sysnormal.starters.security.sso.sso_starter.server.auth;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.AgentAuthDto;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.RefreshTokenRequestDTO;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.TokenRequestDTO;
import com.sysnormal.starters.security.sso.sso_starter.services.auth.AuthenticationService;
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
        return authenticationService.login(agentAuthDto).sendHttpResponse();
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
        return authenticationService.register(agentAuthDto).sendHttpResponse();
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
        return authenticationService.checkTokenFromDto(tokenDto).sendHttpResponse();
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
        return authenticationService.refreshTokenFromDto(refreshTokenDto).sendHttpResponse();
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
        return authenticationService.sendEmailRecoverPasswordFromDto(agentAuthDto).sendHttpResponse();
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
        return authenticationService.passwordChangeFromDto(agentAuthDto).sendHttpResponse();
    }
}
