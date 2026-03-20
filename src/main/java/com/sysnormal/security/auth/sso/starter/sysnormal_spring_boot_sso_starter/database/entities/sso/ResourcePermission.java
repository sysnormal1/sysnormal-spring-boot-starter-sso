package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "resource_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "resource_permissions_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "resource_id",
                                "(coalesce(access_profile_id,-1))",
                                "(coalesce(agent_id,-1))",
                                "(coalesce(start_at,0))",
                                "(coalesce(end_at,0))"
                        }
                )
        }
)
public class ResourcePermission extends BaseSsoEntity<ResourcePermission> {

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "access_profile_id")
    private Long accessProfileId;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "allowed_access", nullable = false)
    @ColumnDefault("1")
    @Check(constraints = "allowed_access in (0,1)")
    @Comment("represents the access according resource type, like execute on method, executable or process, access option url, file, endpoint, or other accessible object, etc")
    private byte allowedAccess = 1;

    @Column(name = "allowed_view", nullable = false)
    @ColumnDefault("1")
    @Check(constraints = "allowed_view in (0,1)")
    @Comment("represents the access to view resource, like view url, view table or column data, view file content, etc")
    private byte allowedView = 1;

    @Column(name = "allowed_create", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "allowed_create in (0,1)")
    private byte allowedCreate = 0;

    @Column(name = "allowed_change", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "allowed_change in (0,1)")
    private byte allowedChange = 0;

    @Column(name = "allowed_delete", nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "allowed_delete in (0,1)")
    private byte allowedDelete = 0;

    @Column(name = "condition", length = Integer.MAX_VALUE)
    private String condition;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_profile_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AccessProfile accessProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Agent agent;

}
