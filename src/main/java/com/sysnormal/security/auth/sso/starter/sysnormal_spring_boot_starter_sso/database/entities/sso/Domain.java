package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "domains",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "domains_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "(coalesce(domain_platform_id, -1))",
                                "(coalesce(domain_side_id, -1))",
                                "name"
                        }
                )
        }
)
public class Domain extends BaseSsoEntity<Domain> {

    @Column(name = "domain_type_id", nullable = false)
    private Long domainTypeId;

    /* only APPLICATION and SERVICE domains run somewhere: null for ECOSYSTEM, DATABASE and CLUSTER */
    @Column(name = "domain_platform_id")
    private Long domainPlatformId;

    /* only APPLICATION and SERVICE domains have a side: null for ECOSYSTEM, DATABASE and CLUSTER */
    @Column(name = "domain_side_id")
    private Long domainSideId;

    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_type_id", insertable = false, updatable = false)
    @JsonIgnore
    private DomainType domainType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_platform_id", insertable = false, updatable = false)
    @JsonIgnore
    private DomainPlatformType domainPlatformType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_side_id", insertable = false, updatable = false)
    @JsonIgnore
    private DomainSide domainSide;

    public static final long SSO_SERVER_ID = 1;
    public static final long SSO_WEBCLIENT_ID = 2;

    public static final Domain SSO_SERVER = new Domain(){{
        setId(SSO_SERVER_ID);
        setIsSysRec((byte) 1);
        setName("SSO SERVER");
        setDomainTypeId(DomainType.SERVICE_ID);
        setDomainPlatformId(DomainPlatformType.DESKTOP_ID);
        setDomainSideId(DomainSide.SERVER_SIDE_ID);
    }};

    public static final Domain SSO_WEBCLIENT = new Domain(){{
        setId(SSO_WEBCLIENT_ID);
        setIsSysRec((byte) 1);
        setName("SSO WEBCLIENT");
        setDomainTypeId(DomainType.APPLICATION_ID);
        setDomainPlatformId(DomainPlatformType.WEB_ID);
        setDomainSideId(DomainSide.CLIENT_SIDE_ID);
    }};




}
