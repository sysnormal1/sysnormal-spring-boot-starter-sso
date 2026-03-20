package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(
        name = "agents_x_access_profiles_x_systems",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "agents_x_access_profiles_x_systems_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "agent_id",
                                "access_profile_id",
                                "system_id"
                        }
                )
        }
)
public class AgentXAccessProfileXSystem extends BaseSsoEntity<AgentXAccessProfileXSystem> {

    @Column(name = "agent_id", nullable = false)
    private Long agentId;

    @Column(name = "access_profile_id", nullable = false)
    private Long accessProfileId;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_profile_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AccessProfile accessProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private System system;



}
