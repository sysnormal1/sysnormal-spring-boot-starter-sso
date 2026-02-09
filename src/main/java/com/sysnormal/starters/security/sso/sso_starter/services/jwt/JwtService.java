package com.sysnormal.starters.security.sso.sso_starter.services.jwt;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.AgentsRepository;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.SystemsRepository;
import com.sysnormal.starters.security.sso.sso_starter.properties.jwt.JwtProperties;
import com.sysnormal.starters.security.sso.sso_starter.server.auth.dtos.AgentAuthDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * jwt service
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


    @Autowired
    AgentsRepository agentsRepository;



    private final Key key;
    private final JwtProperties jwtProperties;
    private final JwtParser jwtParsaer;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.jwtParsaer = Jwts.parser().setSigningKey(key).build();
    }

    public String createToken(AgentAuthDto agentAuthDto) {

        if (agentAuthDto == null) {
            return null;
        }
        logger.debug("creating token for agent {}, identifier {}, systemId {}, accessProfileId {}, expiration {}",
                agentAuthDto.getAgentId(), agentAuthDto.getIdentifier(), agentAuthDto.getSystemId(), agentAuthDto.getAccessProfileId(), agentAuthDto.getExpiration());

        JwtBuilder builder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(agentAuthDto.getAgentId()))
                .claim("agent_id", agentAuthDto.getAgentId())
                .claim("system_id", agentAuthDto.getSystemId())
                .setIssuedAt(new Date());

        // claim opcional
        if (agentAuthDto.getAccessProfileId() != null) {
            builder.claim("access_profile_id", agentAuthDto.getAccessProfileId());
        }

        if (agentAuthDto.getExpiration() != null && agentAuthDto.getExpiration() > 0) {
            builder.setExpiration(new Date(System.currentTimeMillis() + agentAuthDto.getExpiration()));
        }

        return builder.compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key) // mesma key usada no createToken
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createRefreshToken(AgentAuthDto agentAuthDto) {
        agentAuthDto.setExpiration(86400000L);//1 day
        return createToken(agentAuthDto);
    }

    /**
     *
     * @param token
     * @return DefaultDataSwap, containing AgentAuthDto in data property
     */
    public DefaultDataSwap checkToken(String token){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            logger.debug("checking token {}",token);
            if (StringUtils.hasText(token)) {
                Claims claims = jwtParsaer.parseClaimsJws(token).getBody();
                String agentId = String.valueOf(claims.get("agent_id"));
                if (StringUtils.hasText(agentId)) {
                    AgentAuthDto agentAuthDto = new AgentAuthDto();
                    agentAuthDto.setAgentId(Long.valueOf(agentId));
                    agentAuthDto.setSystemId(Long.valueOf(String.valueOf(claims.get("system_id"))));
                    agentAuthDto.setAccessProfileId(Long.valueOf(String.valueOf(claims.get("access_profile_id"))));

                    //result = agentsRepository.checkTokenData(agentAuthDto.getAgentId(),agentAuthDto.getSystemId(), agentAuthDto.getAccessProfileId());

                    result.data = agentAuthDto;
                    result.success = true;
                } else {
                    result.message = "invalid token";
                }
            } else {
                result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
                result.message = "missing data";
            }
        } catch (ExpiredJwtException e) {
            result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
            result.message = "token expired";
            result.setException(e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            result.httpStatusCode = HttpStatus.UNAUTHORIZED.value();
            result.message = "invalid signature";
            result.setException(e);
        } catch (MalformedJwtException e) {
            result.httpStatusCode = HttpStatus.BAD_REQUEST.value();
            result.message = "malformed token";
            result.setException(e);
        } catch (Exception e) {
            result.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            result.message = "unexpected error";
            result.setException(e);
        }
        return result;
    }
}
