package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.jwt;

import com.sysnormal.commons.core.utils_core.TextUtils;
import com.sysnormal.security.auth.auth_core.dtos.AgentAuthDto;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.properties.jwt.JwtSsoProperties;

import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso_client_protector.services.jwt.JwtSsoClientProtectorService;
import com.sysnormal.security.core.security_core.utils.KeyUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * jwt service
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
//@Service //dont anotate @component in starter
@EnableConfigurationProperties(JwtSsoProperties.class)
@Getter
@Setter
public class JwtSsoService extends JwtSsoClientProtectorService {

    private static final Logger logger = LoggerFactory.getLogger(JwtSsoService.class);


    private final JwtSsoProperties jwtSsoProperties;
    private String privatePemFilePath;
    private String privatePem;
    private PrivateKey privateKey;

    public JwtSsoService(JwtSsoProperties jwtSsoProperties) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        super(jwtSsoProperties);
        this.jwtSsoProperties = jwtSsoProperties;
        this.setPrivatePemFilePath(jwtSsoProperties.getPrivateKeyPath());
    }

    public void setPrivatePemFilePath(String privatePemFilePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.privatePemFilePath = privatePemFilePath;
        logger.info("setted private pem file path: '{}'", this.privatePemFilePath);
        if (TextUtils.hasNotNullText(this.privatePemFilePath)) {
            Path path = Path.of(this.privatePemFilePath);
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
            this.setPrivatePem(pem);
        }
    }

    public void setPrivatePem(String privatePem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.privatePem = privatePem;
        logger.info("setted private pem ({} bytes).", this.privatePem.length());

        this.privateKey = KeyUtils.parseRsaPrivateKey(this.privatePem);

        logger.info("Successfully parsed RSA private key.");
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
            jwtBuilder.expiration(new Date(System.currentTimeMillis() + jwtSsoProperties.getDefaultTokenExpiration()));
        }

        return jwtBuilder.compact();
    }

    public String createRefreshToken(AgentAuthDto agentAuthDto) {
        Long oldExpiration = agentAuthDto.getExpiration();
        agentAuthDto.setExpiration(jwtSsoProperties.getDefaultRefreshTokenExpiration());
        String result = createToken(agentAuthDto);
        agentAuthDto.setExpiration(oldExpiration);
        return result;
    }

}
