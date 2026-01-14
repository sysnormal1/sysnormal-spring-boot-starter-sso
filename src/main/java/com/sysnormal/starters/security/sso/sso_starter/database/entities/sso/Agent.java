package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "identifier_type_id", nullable = false)
    @ColumnDefault(IdentifierType.EMAIL_ID + "")
    private Long identifierTypeId = IdentifierType.EMAIL_ID;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "email",length = 512)
    private String email;

    @JsonIgnore
    @Column(name = "security", length = 1000)
    private String password;

    @Column(name = "alias")
    private String alias;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "last_token", length = 1000)
    private String lastToken;

    @Column(name = "last_refresh_token", length = 1000)
    private String lastRefreshToken;

    @Column(name = "last_password_change_token", length = 1000)
    private String lastPasswordChangeToken;

    @Column(name = "token_expiration_time")
    private Long tokenExpirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_type_id", nullable = false, updatable = false, insertable = false)
    @JsonIgnore
    private IdentifierType identifierType = IdentifierType.EMAIL;

    public static final long SYSTEM_ID = 1;

    public static final Agent SYSTEM = new Agent(){{
        setId(SYSTEM_ID);
        setIsSysRec((byte) 1);
        setIdentifier("system@system");
        setEmail("system@system");
        setPassword("system");
    }};
}
