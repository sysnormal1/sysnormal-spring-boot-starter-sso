package com.sysnormal.starters.security.sso.sso_starter.services.jwt;

import com.sysnormal.libs.commons.DefaultDataSwap;
import com.sysnormal.libs.utils.ObjectUtils;
import com.sysnormal.libs.utils.TokenUtils;
import com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso.AgentsRepository;
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
    //private final JwtBuilder jwtBuilder; //not recomended reuse of this object

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.jwtParsaer = Jwts.parser().setSigningKey(key).build();
        //this.jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS256);
    }

    public String createToken(AgentAuthDto agentAuthDto) {

        if (agentAuthDto == null) {
            return null;
        }

        /*@TODO - 2026-03-04 - track origin of informations (systemId and accessProfileId) in ascendent calls of this method and set in agentAuthDto if not setted*/
        logger.debug("creating token for agent {}, identifier {}, systemId {}, accessProfileId {}, expiration {}",
                agentAuthDto.getAgentId(), agentAuthDto.getIdentifier(), agentAuthDto.getSystemId(), agentAuthDto.getAccessProfileId(), agentAuthDto.getExpiration());

        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(agentAuthDto.getAgentId()))
                .claim("agentId", agentAuthDto.getAgentId())
                .claim("systemId", agentAuthDto.getSystemId())
                .setIssuedAt(new Date());

        // claim opcional
        if (agentAuthDto.getAccessProfileId() != null) {
            jwtBuilder.claim("accessProfileId", agentAuthDto.getAccessProfileId());
        }

        if (agentAuthDto.getExpiration() != null) {
            if (agentAuthDto.getExpiration() > 0) {
                jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + agentAuthDto.getExpiration()));
            }
        } else {
            jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getDefaultTokenExpiration()));
        }

        return jwtBuilder.compact();
    }

    public Claims getClaims(String token) {
        Claims result = jwtParsaer
                .parseClaimsJws(token)
                .getBody();
        logger.debug(
                "JWT_VALID subject={} agentId={} accessProfileId={} systemId={} expiresIn={}s",
                result.getSubject(),
                result.get("agentId"),
                result.get("systemId"),
                result.get("accessProfileId"),
                result.getExpiration() != null ? (result.getExpiration().getTime() - System.currentTimeMillis()) / 1000 : 0
        );
        return result;
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
            Long expiresIn = TokenUtils.getExpiration(token); //seconds
            logger.debug("checking token {}, expiresIn {}, now millis {}, seconds remaining {}",token, expiresIn, System.currentTimeMillis(), (expiresIn != null && expiresIn > 0) ? expiresIn - System.currentTimeMillis() / 1000 : "infinit");
            if (StringUtils.hasText(token)) {
                Claims claims = getClaims(token);
                AgentAuthDto agentAuthDto = new AgentAuthDto();
                ObjectUtils.setLongPropertyFromMap(claims,"agentId",agentAuthDto::setAgentId);
                if (agentAuthDto.getAgentId() != null) {
                    ObjectUtils.setLongPropertyFromMap(claims,"systemId",agentAuthDto::setSystemId);
                    ObjectUtils.setLongPropertyFromMap(claims,"accessProfileId",agentAuthDto::setAccessProfileId);
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
