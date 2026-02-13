package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(
        name = "access_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "access_profiles_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class AccessProfile extends BaseSsoEntity<AccessProfile> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "allow_access_to_all_module_routines", nullable = false, length = 1)
    @ColumnDefault("0")
    @Check(constraints = "allow_access_to_all_module_routines in (0,1)")
    private byte allowAccessToAllModuleRoutines = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_data")
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    public static final long SYSTEM_ID = 1;

    public static final AccessProfile SYSTEM = new AccessProfile(){{
        setId(SYSTEM_ID);
        setIsSysRec((byte) 1);
        setName("SYSTEM");
        setAllowAccessToAllModuleRoutines((byte) 1);
    }};

}
