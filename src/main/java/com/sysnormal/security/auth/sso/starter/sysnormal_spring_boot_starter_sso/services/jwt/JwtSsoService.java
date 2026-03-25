package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt;

import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtProperties;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso_client_protector.services.jwt.JwtService;
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
//@Service //dont anotate @component in starter
@EnableConfigurationProperties(JwtProperties.class)
public class JwtSsoService extends JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtSsoService.class);

    private final JwtProperties jwtProperties;
    private final String privatePem;
    private final PrivateKey privateKey;

    public JwtSsoService(JwtProperties jwtProperties) {
        super(jwtProperties);
        this.jwtProperties = jwtProperties;

        String privateKeyPath = jwtProperties.getPrivateKeyPath();

        logger.info("Initializing JwtSsoService (private key phase) with path (from spring.jwt.private-key-path): '{}'", privateKeyPath);

        try {
            Path path = Path.of(privateKeyPath);

            logger.debug("Resolved private key path to absolute path: '{}'", path.toAbsolutePath());

            if (!Files.exists(path)) {
                logger.error("Private key file does not exist at path: '{}'", path.toAbsolutePath());
                throw new IllegalStateException("Private key file not found: " + path.toAbsolutePath());
            }

            if (!Files.isReadable(path)) {
                logger.error("Private key file is not readable at path: '{}'", path.toAbsolutePath());
                throw new IllegalStateException("Private key file is not readable: " + path.toAbsolutePath());
            }

            logger.info("Private key file found. Attempting to read file...");

            String pem = Files.readString(path);
            this.privatePem = pem;

            logger.info("Successfully read private key file ({} bytes).", pem.length());

            this.privateKey = KeyUtils.parseRsaPrivateKey(pem);

            logger.info("Successfully parsed RSA private key.");

        } catch (Exception e) {
            logger.error(
                    "Failed to initialize JwtSsoService (private key phase) with path '{}'. Error: {}",
                    privateKeyPath,
                    e.getMessage(),
                    e
            );

            throw new IllegalStateException("Failed to initialize JwtSsoService (private key)", e);
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
