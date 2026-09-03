package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(
        name = "domain_sides",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "domain_sides_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class DomainSide extends BaseSsoEntity<DomainSide> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "is_server", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "is_server in (0,1)")
    private byte isServer = 0;

    @Column(name = "is_client", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "is_client in (0,1)")
    private byte isClient = 0;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    public static final long WITHOUT_SIDE_ID = 0;
    public static final long SERVER_SIDE_ID = 1;
    public static final long CLIENT_SIDE_ID = 2;

    public static final DomainSide SERVER_SIDE = new DomainSide(){{
        setId(SERVER_SIDE_ID);
        setIsSysRec((byte) 1);
        setName("SERVER SIDE");
        setIsServer((byte) 1);
    }};

    public static final DomainSide CLIENT_SIDE = new DomainSide(){{
        setId(CLIENT_SIDE_ID);
        setIsSysRec((byte) 1);
        setName("CLIENT SIDE");
        setIsClient((byte) 1);
    }};

    public static final DomainSide WITHOUT_SIDE = new DomainSide(){{
        setId(WITHOUT_SIDE_ID);
        setIsSysRec((byte) 1);
        setName("WITHOUT SIDE");
    }};


}
