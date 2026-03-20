package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.auth.github;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.core.utils_core.HttpUtils;
import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.Agent;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso.IdentifierType;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.helpers.security.PasswordUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.auth.github.GitHubAuthProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.properties.security.SecurityProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.services.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@EnableConfigurationProperties({GitHubAuthProperties.class, SecurityProperties.class})
@ConditionalOnProperty(prefix = "spring.auth.github", name = "enabled", havingValue = "true")
public class GitHubAuthService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubAuthService.class);

    private static final String GITHUB_AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_AGENT_URL = "https://api.github.com/user";
    private static final String GITHUB_AGENT_EMAIL_URL = "https://api.github.com/user/emails";


    @Autowired
    ObjectMapper objectMapper;


    private GitHubAuthProperties properties;
    private SecurityProperties securityProperties;
    private AuthenticationService authenticationService;
    private AgentsRepository agentRepository;


    public GitHubAuthService(
            GitHubAuthProperties properties,
            SecurityProperties securityProperties,
            AuthenticationService authenticationService,
            AgentsRepository agentRepository
    ) {
        this.properties = properties;
        this.securityProperties = securityProperties;
        this.authenticationService = authenticationService;
        this.agentRepository = agentRepository;
    }

    public DefaultDataSwap getLoginUrl() {
        DefaultDataSwap result = new DefaultDataSwap();
        result.data = GITHUB_AUTH_URL + "?"
                + "client_id=" + properties.getClientId()
                + "&scope=user:email"
                + "&redirect_uri=" + properties.getRedirectUri();
        result.success = true;
        return result;
    }

    public DefaultDataSwap handleCode(AgentAuthDto agentAuthDto) {
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            logger.debug("handling GitHub code {}, redirectUri {}", agentAuthDto.getCode(), agentAuthDto.getRedirectUri());

            // Trocar o código pelo access token
            String accessToken = exchangeCodeForToken(agentAuthDto);

            logger.debug("Successfully obtained access token");

            // Obter informações do usuário
            Map<String, Object> oAuth2AgentInfo = getOAuth2AgentInfo(accessToken);

            logger.debug("OAuth2 agent info: {}", oAuth2AgentInfo);

            // Obter email (pode estar em um endpoints separado se não estiver público)
            String email = getEmail(oAuth2AgentInfo, accessToken);

            if (StringUtils.hasText(email)) {
                Optional<Agent> agent = agentRepository.findByIdentifierTypeIdAndIdentifierOrEmail(
                        IdentifierType.EMAIL_ID,
                        email.trim().toLowerCase(),
                        email.trim().toLowerCase()
                );
                if (agent.isPresent()) {
                    agentAuthDto.setAgentId(agent.get().getId());
                    agentAuthDto.setIdentifierTypeId(agent.get().getIdentifierTypeId());
                    agentAuthDto.setIdentifier(agent.get().getIdentifier());
                    agentAuthDto.setEmail(email.trim().toLowerCase());
                    result = authenticationService.getAuthDataResult(agentAuthDto, agent, false, null,  true, null);
                } else {
                    String password = PasswordUtils.generateCompliantPassword(email, securityProperties.getPasswordRules());
                    agentAuthDto.setIdentifierTypeId(IdentifierType.EMAIL_ID);
                    agentAuthDto.setIdentifier(email);
                    agentAuthDto.setEmail(email);
                    agentAuthDto.setPassword(password);
                    result = authenticationService.register(agentAuthDto);
                }
            } else {
                throw new Exception("Agent info does not contain email");
            }
        } catch (Exception e) {
            result.setException(e);
            logger.error("Error handling GitHub code", e);
        }
        return result;
    }

    private String exchangeCodeForToken(AgentAuthDto agentAuthDto) throws Exception {
        HashMap<String, String> body = new HashMap<>();
        body.put("client_id", properties.getClientId());
        body.put("client_secret", properties.getClientSecret());
        body.put("code", agentAuthDto.getCode());
        body.put("redirect_uri", agentAuthDto.getRedirectUri());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_TOKEN_URL))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(HttpUtils.buildQuery(body)))
                .build();

        logger.debug("Request to GitHub token URL");

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        logger.debug("GitHub token response: {}", response.body());
        Map<String, Object> json = objectMapper.readValue(response.body(), Map.class);

        if (json.containsKey("error")) {
            throw new Exception("GitHub OAuth error: " + json.get("error_description"));
        }

        return (String) json.get("access_token");
    }

    private Map<String, Object> getOAuth2AgentInfo(String accessToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_AGENT_URL))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get agent info: " + response.body());
        }

        return objectMapper.readValue(response.body(), Map.class);
    }

    private String getEmail(Map<String, Object> agentInfo, String accessToken) throws Exception {
        // Primeiro tenta pegar o email direto do agentInfo
        String email = (String) agentInfo.get("email");

        if (StringUtils.hasText(email)) {
            return email;
        }

        // Se não tiver email público, busca no endpoints de emails
        logger.debug("Email not public, fetching from emails endpoints");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_AGENT_EMAIL_URL))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get agent emails: " + response.body());
        }

        // GitHub retorna uma lista de emails
        Object[] emails = objectMapper.readValue(response.body(), Object[].class);

        // Procura pelo email primário e verificado
        for (Object emailObj : emails) {
            Map<String, Object> emailData = (Map<String, Object>) emailObj;
            Boolean primary = (Boolean) emailData.get("primary");
            Boolean verified = (Boolean) emailData.get("verified");

            if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                return (String) emailData.get("email");
            }
        }

        // Se não encontrar email primário, pega o primeiro verificado
        for (Object emailObj : emails) {
            Map<String, Object> emailData = (Map<String, Object>) emailObj;
            Boolean verified = (Boolean) emailData.get("verified");

            if (Boolean.TRUE.equals(verified)) {
                return (String) emailData.get("email");
            }
        }

        throw new Exception("No verified email found for GitHub agent");
    }
}