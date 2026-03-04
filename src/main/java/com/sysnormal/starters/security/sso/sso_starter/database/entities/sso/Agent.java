package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sysnormal.starters.security.sso.sso_starter.configs.AppInitializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * agent
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Entity
@Table(
        name = "agents",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "agents_u1",
                        columnNames = {
                               /*
                            people only is differenced by identifier doc type and document, not by origin
                            "(coalesce(parent_id, -1))","record_status_id","data_origin_id","(coalesce(table_origin_id, -1))","(coalesce(id_at_origin, -1))",
                             */
                                "identifier_type_id", "identifier"
                        }
                )
        }
)
@Getter
@Setter
public class Agent extends BaseSsoEntity<Agent> {
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Column(name = "identifier_type_id", nullable = false)
    @ColumnDefault(IdentifierType.EMAIL_ID + "")
    private Long identifierTypeId = IdentifierType.EMAIL_ID;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "email",length = 512)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 1000)
    private String password;

    @Column(name = "alias")
    private String alias;

    @Column(name = "last_token", length = 1000)
    private String lastToken;

    @Column(name = "last_refresh_token", length = 1000)
    private String lastRefreshToken;

    @Column(name = "last_password_change_token", length = 1000)
    private String lastPasswordChangeToken;

    @Column(name = "token_expiration_time")
    private Long tokenExpirationTime;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_type_id", nullable = false, updatable = false, insertable = false)
    @JsonIgnore
    private IdentifierType identifierType = IdentifierType.EMAIL;

    public static final long SYSTEM_ID = 1;

    public static final Agent SYSTEM = new Agent(){{
        setId(SYSTEM_ID);
        setIsSysRec((byte) 1);
        setIdentifierTypeId(IdentifierType.EMAIL_ID);

        String systemEmail = AppInitializer.getInstance().getENV().getProperty(
                "spring.security.system-user.email",
                java.lang.System.getenv().getOrDefault("SYSTEM_USER_EMAIL", "system@system")
        );
        String systemPassword = AppInitializer.getInstance().getENV().getProperty(
                "spring.security.system-user.password",
                java.lang.System.getenv().getOrDefault("SYSTEM_USER_PASSWORD", "system")
        );

        logger.debug("xxxxxxxxxxxxx agent static creation {} {}", systemEmail, systemPassword);


        setIdentifier(systemEmail);
        setEmail(systemEmail);
        setPassword(encoder.encode(systemPassword));
    }};
}
