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
        name = "system_platform_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "system_platform_types_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class SystemPlatformType extends BaseSsoEntity<SystemPlatformType> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "is_desktop", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "is_desktop in (0,1)")
    private byte isDesktop = 0;

    @Column(name = "is_web", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "is_web in (0,1)")
    private byte isWeb = 0;

    @Column(name = "is_mobile", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "is_mobile in (0,1)")
    private byte isMobile = 0;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;


    public static final long DESKTOP_ID = 1;
    public static final long WEB_ID = 2;
    public static final long MOBILE_ID = 3;

    public static final SystemPlatformType DESKTOP = new SystemPlatformType(){{
        setId(DESKTOP_ID);
        setIsSysRec((byte) 1);
        setName("DESKTOP");
        setIsDesktop((byte) 1);
    }};

    public static final SystemPlatformType WEB = new SystemPlatformType(){{
        setId(WEB_ID);
        setIsSysRec((byte) 1);
        setName("WEB");
        setIsWeb((byte) 1);
    }};

    public static final SystemPlatformType MOBILE = new SystemPlatformType(){{
        setId(MOBILE_ID);
        setIsSysRec((byte) 1);
        setName("MOBILE");
        setIsMobile((byte) 1);
    }};


}
