package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt;

import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtProperties;
import com.sysnormal.security.core.security_core.services.jwt.JwtCoreService;
import com.sysnormal.security.core.security_core.utils.KeyUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * jwt service
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtSsoService extends JwtCoreService {

    private static final Logger logger = LoggerFactory.getLogger(JwtSsoService.class);

    private final JwtProperties jwtProperties;
    private final String privatePem;
    private final String publicPem;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;



    public JwtSsoService(JwtProperties jwtProperties) {
        super();
        this.jwtProperties = jwtProperties;

        try {
            this.privatePem = Files.readString(Path.of(jwtProperties.getPrivateKeyPath()));
            this.publicPem = Files.readString(Path.of(jwtProperties.getPublicKeyPath()));
            this.privateKey = KeyUtils.parseRsaPrivateKey(privatePem);
            this.publicKey = KeyUtils.parseRsaPublicKey(publicPem);
            buildJwtParser(this.publicKey);
        } catch (Exception e) {
            throw new IllegalStateException("Error", e);
        }
    }

    public String createToken(AgentAuthDto agentAuthDto) {

        if (agentAuthDto == null) {
            return null;
        }

        /*@TODO - 2026-03-04 - track origin of informations (systemId and accessProfileId) in ascendent calls of this method and set in agentAuthDto if not setted*/
        logger.debug("creating token for agent {}, identifier {}, systemId {}, accessProfileId {}, expiration {}",
                agentAuthDto.getAgentId(), agentAuthDto.getIdentifier(), agentAuthDto.getSystemId(), agentAuthDto.getAccessProfileId(), agentAuthDto.getExpiration());

        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .subject(String.valueOf(agentAuthDto.getAgentId()))
                .claim("agentId", agentAuthDto.getAgentId())
                .claim("systemId", agentAuthDto.getSystemId())
                .issuedAt(new Date());

        // claim opcional
        if (agentAuthDto.getAccessProfileId() != null) {
            jwtBuilder.claim("accessProfileId", agentAuthDto.getAccessProfileId());
        }

        if (agentAuthDto.getExpiration() != null) {
            if (agentAuthDto.getExpiration() > 0) {
                jwtBuilder.expiration(new Date(System.currentTimeMillis() + agentAuthDto.getExpiration()));
            }
        } else {
            jwtBuilder.expiration(new Date(System.currentTimeMillis() + jwtProperties.getDefaultTokenExpiration()));
        }

        return jwtBuilder.compact();
    }

    public String createRefreshToken(AgentAuthDto agentAuthDto) {
        Long oldExpiration = agentAuthDto.getExpiration();
        agentAuthDto.setExpiration(jwtProperties.getDefaultRefreshTokenExpiration());
        String result = createToken(agentAuthDto);
        agentAuthDto.setExpiration(oldExpiration);
        return result;
    }

}
