package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(
        name = "record_status",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "record_status_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class RecordStatus extends BaseSsoEntity<RecordStatus> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("1")
    private byte isActive = 1;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;


    // Constantes equivalentes
    public static final long ACTIVE_ID = 1;
    public static final long INACTIVE_ID = 2;

    public static final RecordStatus ACTIVE = new RecordStatus(){{
        setId(ACTIVE_ID);
        setIsSysRec((byte) 1);
        setName("ACTIVE");
        setIsActive((byte) 1);
    }};
    public static final RecordStatus INACTIVE = new RecordStatus(){{
        setId(INACTIVE_ID);
        setIsSysRec((byte) 1);
        setName("INACTIVE");
        setIsActive((byte) 0);
    }};
}