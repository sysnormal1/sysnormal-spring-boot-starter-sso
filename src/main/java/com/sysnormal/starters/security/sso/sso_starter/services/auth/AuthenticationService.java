package com.sysnormal.starters.security.sso.sso_starter.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Agent;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.IdentifierType;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.RecordStatus;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.System;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.ResourcePermissionsRepository;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.SystemsRepository;
import com.sysnormal.starters.security.sso.sso_starter.properties.jwt.JwtProperties;
import com.sysnormal.starters.security.sso.sso_starter.properties.security.SecurityProperties;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.AgentAuthDto;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.RefreshTokenRequestDTO;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.TokenRequestDTO;
import com.sysnormal.starters.security.sso.sso_starter.services.jwt.JwtService;
import com.sysnormal.starters.security.sso.sso_starter.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * authentication service
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Service
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final SecurityProperties properties;

    private final JwtProperties jwtProperties;


    @Autowired
    JwtService jwtService;

    @Autowired
    SystemsRepository systemsRepository;

    @Autowired
    AgentsRepository agentsRepository;

    @Autowired
    ResourcePermissionsRepository resourcePermissionsRepository;

    @Autowired
    MailService mailService;

    @Autowired
    ObjectMapper objectMapper;


    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthenticationService(SecurityProperties properties,  JwtProperties jwtProperties) {
        this.properties = properties;
        this.jwtProperties = jwtProperties;
    }


    public DefaultDataSwap getAuthDataResult(
            AgentAuthDto agentAuthDto,
            Optional<Agent> optionalAgent,
            Boolean checkPassword,
            String token,
            Boolean returnRefreshToken,
            String refreshToken
    ) throws JsonProcessingException {
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"getAuthDataResult");
        DefaultDataSwap result = new DefaultDataSwap();
        if (optionalAgent.isPresent()) {
            if (!checkPassword || (checkPassword && encoder.matches(agentAuthDto.getPassword(), optionalAgent.get().getPassword()))) {
                if (optionalAgent.get().getDeletedAt() == null && Objects.equals(RecordStatus.ACTIVE_ID,optionalAgent.get().getRecordStatusId())) {

                    Map<String,Object> dataObject = new HashMap<>();
                    if (!StringUtils.hasText(token)) {
                        optionalAgent.get().setLastToken(jwtService.createToken(agentAuthDto));
                        dataObject.put("token", optionalAgent.get().getLastToken());
                    } else {
                        dataObject.put("token", token);
                    }
                    if (returnRefreshToken) {
                        optionalAgent.get().setLastRefreshToken(jwtService.createRefreshToken(agentAuthDto));
                        dataObject.put("refreshToken", optionalAgent.get().getLastRefreshToken());
                    }
                    if (!StringUtils.hasText(token) || returnRefreshToken) {
                        agentsRepository.save(optionalAgent.get());
                    }
                    optionalAgent.get().setPassword(null);
                    dataObject.put("agent", objectMapper.convertValue(optionalAgent.get(), Map.class));
                    result.data = dataObject;
                    result.httpStatusCode = HttpStatus.OK.value();
                    result.success = true;
                } else {
                    result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
                    result.message = "agent is not active";
                }
            } else {
                result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
                result.message = "password not match";
            }
        } else {
            result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
            result.message = "agent not found";
        }
        logger.debug("getAuthDataResult return is {}",objectMapper.convertValue(result, Map.class));
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"getAuthDataResult");
        return result;
    }

    private DefaultDataSwap getAuthDataResult(
            AgentAuthDto agentAuthDto,
            Optional<Agent> agent,
            String token
    ) throws JsonProcessingException {
        return getAuthDataResult(agentAuthDto, agent,false, token, false, null);
    }

    public DefaultDataSwap login(AgentAuthDto agentAuthDto){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"login");
        DefaultDataSwap result = new DefaultDataSwap();
        try {

            if (agentAuthDto != null) {
                if (StringUtils.hasText(agentAuthDto.getPassword())) {
                    String identifier = agentAuthDto.getIdentifier();
                    Optional<Agent> optionalAgent = Optional.empty();
                    if (StringUtils.hasText(identifier)) {
                        Long identifierTypeId = agentAuthDto.getIdentifierTypeId();
                        if (identifierTypeId == null) {
                            if (mailService.isValidEmail(identifier)) {
                                identifierTypeId = IdentifierType.EMAIL_ID;
                            } else {
                                identifierTypeId = IdentifierType.IDENTIFIER_ID;
                            }
                        }
                        optionalAgent = agentsRepository.findByIdentifierTypeIdAndIdentifier(
                                identifierTypeId,
                                identifier.trim().toLowerCase()
                        );
                    } else {
                        String email = agentAuthDto.getEmail();
                        if (StringUtils.hasText(email)) {
                            optionalAgent = agentsRepository.findByEmail(email.trim().toLowerCase());
                        } else {
                            result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                            result.message = "missing identifier or email";
                        }
                    }
                    if (optionalAgent.isPresent()) {
                        if (RecordStatus.ACTIVE_ID == optionalAgent.get().getRecordStatusId()) {
                            agentAuthDto.setAgentId(optionalAgent.get().getId());
                            agentAuthDto.setExpiration(jwtProperties.getDefaultTokenExpiration());

                            if (agentAuthDto.getSystemId() != null) {
                                //check if system exists
                                Optional<System> optionalSystem = systemsRepository.findById(agentAuthDto.getSystemId());

                                if (optionalSystem.isPresent()) {
                                    agentAuthDto.setSystemId(optionalSystem.get().getId());

                                    agentAuthDto.setAccessProfileId(agentsRepository.getRelationAccessProfile(optionalAgent.get().getId(), optionalSystem.get().getId()));

                                    //check if user is relation to the system with direct agentxsystem or accessprofilexsystem
                                    boolean agentIsRelationedToSystem = agentsRepository.agentIsRelationToSystem(optionalAgent.get().getId(), optionalSystem.get().getId()) != 0;



                                    if (agentIsRelationedToSystem) {
                                        result = getAuthDataResult(
                                                agentAuthDto,
                                                optionalAgent,
                                                true,
                                                null,
                                                true,
                                                null
                                        );
                                    } else {
                                        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                                        result.message = "agent is not relation to system";
                                    }
                                } else {
                                    result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                                    result.message = "system not found";
                                }
                            } else {
                                result = getAuthDataResult(
                                        agentAuthDto,
                                        optionalAgent,
                                        true,
                                        null,
                                        true,
                                        null
                                );
                            }
                        } else {
                            result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                            result.message = "agent is not active";
                        }
                    } else if (!StringUtils.hasText(result.message)) {
                        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                        result.message = "agent not found";
                    }
                } else {
                    result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                    result.message = "missing password";
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"login");
        return result;
    }

    public DefaultDataSwap passworRulesCheck(String password) {
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (StringUtils.hasText(password)) {
                if (password.length() < properties.getPasswordRules().getMinLength()) {
                    result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                    result.message = "password length less than " + properties.getPasswordRules().getMinLength() + " characters";
                } else {
                    result.success = true;
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "empty password";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap register(AgentAuthDto agentAuthDto){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"register");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (agentAuthDto != null) {
                Long identifierTypeId = agentAuthDto.getIdentifierTypeId();
                String identifier = agentAuthDto.getIdentifier();
                String email = agentAuthDto.getEmail();
                if (!StringUtils.hasText(identifier)) {
                    identifier = email;
                } else if (!StringUtils.hasText(email) && mailService.isValidEmail(identifier)) {
                    email = identifier;
                }
                if (StringUtils.hasText(identifier)) {
                    if (identifierTypeId == null) {
                        if (mailService.isValidEmail(identifier)) {
                            identifierTypeId = IdentifierType.EMAIL_ID;
                        } else {
                            identifierTypeId = IdentifierType.IDENTIFIER_ID;
                        }
                    }
                    if (StringUtils.hasText(agentAuthDto.getPassword())) {
                        Optional<Agent> agent = agentsRepository.findByIdentifierTypeIdAndIdentifierOrEmail(
                                identifierTypeId,
                                StringUtils.hasText(identifier) ? identifier.trim().toLowerCase() : identifier,
                                StringUtils.hasText(email) ? email.trim().toLowerCase() : email
                        );
                        if (agent.isEmpty()) {
                            result = passworRulesCheck(agentAuthDto.getPassword());
                            if (result.success) {
                                Agent newAgent = new Agent();
                                newAgent.setIdentifierTypeId(identifierTypeId);
                                newAgent.setIdentifier(identifier);
                                newAgent.setEmail(email);
                                newAgent.setPassword(encoder.encode(agentAuthDto.getPassword()));
                                agentsRepository.save(newAgent);
                                result = getAuthDataResult(agentAuthDto, agentsRepository.findByIdentifierTypeIdAndIdentifier(newAgent.getIdentifierTypeId(), newAgent.getIdentifier()), false, null, true, null);
                            }
                        } else {
                            result.httpStatusCode = HttpStatus.CONFLICT.value();
                            result.message = "agent already exists";
                        }
                    } else {
                        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                        result.message = "missing password";
                    }
                } else {
                    result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                    result.message = "missing identifier or email";
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"register");
        return result;
    }


    public DefaultDataSwap checkTokenFromDto(TokenRequestDTO tokenRequest) {
        return checkToken(tokenRequest != null ? tokenRequest.getToken() : null);
    }

    public DefaultDataSwap checkToken(String token){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"checkToken");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (StringUtils.hasText(token)) {
                result = jwtService.checkToken(token);
                if (result.success) {
                    AgentAuthDto agentAuthDto = (AgentAuthDto) result.data;
                    result = getAuthDataResult(agentAuthDto, agentsRepository.findById(agentAuthDto.getAgentId()),token);
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"checkToken");
        return result;
    }

    public DefaultDataSwap refreshTokenFromDto(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshToken(refreshTokenRequestDTO != null ? refreshTokenRequestDTO.getRefreshToken() : null);
    }

    public DefaultDataSwap refreshToken(String refreshToken){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"refreshToken");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (StringUtils.hasText(refreshToken)) {
                result = jwtService.checkToken(refreshToken);
                if (result.success) {
                    AgentAuthDto agentAuthDto = (AgentAuthDto) result.data;
                    result = getAuthDataResult(agentAuthDto,agentsRepository.findById(agentAuthDto.getAgentId()),false, null, true, null);
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"refreshToken");
        return result;
    }

    public DefaultDataSwap sendEmailRecoverPasswordFromDto(AgentAuthDto agentAuthDto){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"sendEmailRecoverPasswordFromDto");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (agentAuthDto != null) {
                String email = agentAuthDto.getEmail();
                if (!StringUtils.hasText(email)) {
                    email = agentAuthDto.getIdentifier();
                }
                if (StringUtils.hasText(email)) {
                    if (mailService.isValidEmail(email)) {
                        Optional<Agent> agent = agentsRepository.findByEmail(email.trim().toLowerCase());
                        if (agent.isPresent()) {
                            agentAuthDto.setExpiration(jwtProperties.getDefaultRefreshTokenExpiration());
                            agent.get().setLastPasswordChangeToken(jwtService.createToken(agentAuthDto));
                            agentsRepository.save(agent.get());
                            String subject = "Password Recover";
                            String text = "Follow this link to create a new password: " + agentAuthDto.getPasswordChangeInterfacePath() + "/" + agent.get().getLastPasswordChangeToken();
                            String html = "Follow this link to create a new password: <br /><a href=\"" + agentAuthDto.getPasswordChangeInterfacePath() + "/" + agent.get().getLastPasswordChangeToken() + "\">Change password</a>";

                            mailService.sendEmail(email, subject, text, html);

                            result.success = true; //sendMail throws exception if error
                        } else {
                            result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                            result.message = "agent not found";
                        }
                    } else {
                        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                        result.message = "invalid email";
                    }
                } else {
                    result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                    result.message = "missing data";
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"sendEmailRecoverPasswordFromDto");
        return result;
    }

    public DefaultDataSwap passwordChangeFromDto(AgentAuthDto agentAuthDto){
        logger.debug("INIT {}.{}",this.getClass().getSimpleName(),"passwordChangeFromDto");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (agentAuthDto != null && StringUtils.hasText(agentAuthDto.getToken()) && StringUtils.hasText(agentAuthDto.getPassword())) {
                result = checkToken(agentAuthDto.getToken());
                if (result.success) {
                    result.success = false;
                    Map<String,Object> dataObject = (Map<String, Object>) result.data;
                    Map<String,Object> agentObject = (Map<String, Object>) dataObject.getOrDefault("agent",null);
                    Optional<Agent> agent = agentsRepository.findById(Long.valueOf(String.valueOf(agentObject.getOrDefault("id",null))));
                    if (agent.isPresent()) {
                        if (agentAuthDto.getToken().equals(agent.get().getLastPasswordChangeToken())) {

                            result = passworRulesCheck(agentAuthDto.getPassword());
                            if (result.success) {
                                agent.get().setPassword(encoder.encode(agentAuthDto.getPassword()));
                                agentsRepository.save(agent.get());
                                result.success = true;
                            }
                        } else {
                            result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                            result.message = "token not match";
                        }
                    } else {
                        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                        result.message = "agent not found";
                    }
                }
            } else {
                result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
                result.message = "missing data";
            }
        } catch (Exception e) {
            result.setException(e);
        }
        logger.debug("END {}.{}",this.getClass().getSimpleName(),"passwordChangeFromDto");
        return result;
    }

}
