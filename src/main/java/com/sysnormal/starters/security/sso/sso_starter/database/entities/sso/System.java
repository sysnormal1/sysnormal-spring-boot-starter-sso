package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "systems",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "systems_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "system_platform_id",
                                "system_side_id",
                                "name"
                        }
                )
        }
)
public class System extends BaseSsoEntity<System> {

    @Column(name = "system_platform_id", nullable = false)
    private Long systemPlatformId;

    @Column(name = "system_side_id", nullable = false)
    private Long systemSideId;

    @Column(name = "name", length = 127, nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_platform_id", insertable = false, updatable = false)
    @JsonIgnore
    private SystemPlatformType systemPlatformType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_side_id", insertable = false, updatable = false)
    @JsonIgnore
    private SystemSide systemSide;

    public static final long SSO_SERVER_ID = 1;
    public static final long SSO_WEBCLIENT_ID = 2;

    public static final System SSO_SERVER = new System(){{
        setId(SSO_SERVER_ID);
        setIsSysRec((byte) 1);
        setName("SSO SERVER");
        setSystemPlatformId(SystemPlatformType.DESKTOP_ID);
        setSystemSideId(SystemSide.SERVER_SIDE_ID);
    }};

    public static final System SSO_WEBCLIENT = new System(){{
        setId(SSO_WEBCLIENT_ID);
        setIsSysRec((byte) 1);
        setName("SSO WEBCLIENT");
        setSystemPlatformId(SystemPlatformType.WEB_ID);
        setSystemSideId(SystemSide.CLIENT_SIDE_ID);
    }};




}